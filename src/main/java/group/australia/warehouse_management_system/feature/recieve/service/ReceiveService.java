package group.australia.warehouse_management_system.feature.recieve.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import group.australia.warehouse_management_system.common.exceptions.BadRequestException;
import group.australia.warehouse_management_system.common.exceptions.ConflictException;
import group.australia.warehouse_management_system.common.exceptions.NotFoundException;
import group.australia.warehouse_management_system.feature.SSCC.domain.Sscc;
import group.australia.warehouse_management_system.feature.SSCC.repository.SsccRepositoryPort;
import group.australia.warehouse_management_system.feature.item.domain.Item;
import group.australia.warehouse_management_system.feature.item.repository.ItemRepositoryPort;
import group.australia.warehouse_management_system.feature.location.domain.Location;
import group.australia.warehouse_management_system.feature.location.repository.LocationRepositoryAdapter;
import group.australia.warehouse_management_system.feature.recieve.dto.CreateRecieverDto;
import group.australia.warehouse_management_system.feature.warehouse.domain.Warehouse;
import group.australia.warehouse_management_system.feature.warehouse.repository.WarehouseRepositoryPort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReceiveService {

    private final WarehouseRepositoryPort warehouses;
    private final SsccRepositoryPort ssccs;
    private final LocationRepositoryAdapter locations;
    private final ItemRepositoryPort items;

    @Transactional
     public Item recieve(CreateRecieverDto request) {
        String warehouseNumber = toWarehouseNumber(request.warehouseNumber());

        Warehouse warehouse = warehouses.findByWarehouseNumber(warehouseNumber)
                .orElseThrow(() -> new NotFoundException("Warehouse not found: " + warehouseNumber));
        if (!warehouse.isActive()) {
            throw new ConflictException("Warehouse is inactive: " + warehouseNumber);
        }

        Sscc sscc = ssccs.findBySscc(request.sscc())
                .orElseThrow(() -> new NotFoundException("SSCC not found: " + request.sscc()));
        if (sscc.getReceivedTimestamp() != null) {
            throw new ConflictException("SSCC already received: " + request.sscc());
        }

        Location location = locations.findByLocationCode(warehouse.getId(), request.locationCode())
                .orElseThrow(() -> new NotFoundException("Location not found: " + request.locationCode()));

        String itemNumber = deriveItemNumber(sscc.getCartonHeader().getBarcode());

        Item item = items.findByItemNumberAndLocationId(itemNumber, location.getId())
                .map(existing -> existing.toBuilder()
                        .quantity(existing.getQuantity() + 1)
                        .build())
                .orElseGet(() -> Item.builder()
                        .itemNumber(itemNumber)
                        .quantity(1)
                        .location(location)
                        .cartonHeader(sscc.getCartonHeader())
                        .build());
        Item saved = items.save(item);

        
        ssccs.save(sscc.toBuilder()
                .receivedTimestamp(LocalDateTime.now())
                .build());

        return saved;
    }

    private String toWarehouseNumber(String raw) {
        try {
            return String.format("WH-%03d", Integer.parseInt(raw));
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid warehouse number: " + raw);
        }
    }

    private String deriveItemNumber(String barcode) {
        return barcode; 
    }
}