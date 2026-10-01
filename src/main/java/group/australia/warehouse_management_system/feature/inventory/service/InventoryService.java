package group.australia.warehouse_management_system.feature.inventory.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import group.australia.warehouse_management_system.common.exceptions.BadRequestException;
import group.australia.warehouse_management_system.common.exceptions.NotFoundException;
import group.australia.warehouse_management_system.feature.inventory.dto.InventoryItemDetailResponse;
import group.australia.warehouse_management_system.feature.inventory.dto.InventoryItemResponse;
import group.australia.warehouse_management_system.feature.item.domain.Item;
import group.australia.warehouse_management_system.feature.item.repository.ItemRepositoryPort;
import group.australia.warehouse_management_system.feature.location.domain.Location;
import group.australia.warehouse_management_system.feature.location.repository.LocationRepositoryAdapter;
import group.australia.warehouse_management_system.feature.warehouse.domain.Warehouse;
import group.australia.warehouse_management_system.feature.warehouse.repository.WarehouseRepositoryPort;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InventoryService {

    private final WarehouseRepositoryPort warehouses;
    private final LocationRepositoryAdapter locations;
    private final ItemRepositoryPort items;

    public List<InventoryItemResponse> listItems(String warehouseNumber) {
        Warehouse warehouse = findWarehouse(warehouseNumber);
        return items.findAllByWarehouseId(warehouse.getId()).stream()
                .map(i -> new InventoryItemResponse(
                        i.getItemNumber(),
                        i.getLocation().getLocationCode(),
                        i.getQuantity()))
                .toList();
    }

    public InventoryItemDetailResponse getItem(String warehouseNumber, String itemNumber, String locationCode) {
        Warehouse warehouse = findWarehouse(warehouseNumber);

        Location location = locations.findByLocationCode(warehouse.getId(), locationCode)
                .orElseThrow(() -> new NotFoundException("Location not found: " + locationCode));

        Item item = items.findByItemNumberAndLocationId(itemNumber, location.getId())
                .orElseThrow(() -> new NotFoundException(
                        "Item " + itemNumber + " not found at location " + locationCode));

        var carton = item.getCartonHeader();
        return new InventoryItemDetailResponse(
                item.getId(),
                item.getItemNumber(),
                location.getLocationCode(),
                item.getQuantity(),
                new InventoryItemDetailResponse.CartonHeaderInfo(
                        carton.getId(), carton.getBarcode(), carton.getDescription()),
                item.getCreationTimestamp(),
                item.getUpdateTimestamp());
    }

    private Warehouse findWarehouse(String raw) {
        String warehouseNumber;
        try {
            warehouseNumber = String.format("WH-%03d", Integer.parseInt(raw));
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid warehouse number: " + raw);
        }
        return warehouses.findByWarehouseNumber(warehouseNumber)
                .orElseThrow(() -> new NotFoundException("Warehouse not found: " + warehouseNumber));
    }
}