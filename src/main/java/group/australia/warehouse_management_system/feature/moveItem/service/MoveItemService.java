package group.australia.warehouse_management_system.feature.moveItem.service;

import group.australia.warehouse_management_system.common.exceptions.BadRequestException;
import group.australia.warehouse_management_system.common.exceptions.NotFoundException;
import group.australia.warehouse_management_system.feature.item.domain.Item;
import group.australia.warehouse_management_system.feature.item.service.ItemService;
import group.australia.warehouse_management_system.feature.location.repository.LocationRepositoryAdapter;
import group.australia.warehouse_management_system.feature.moveItem.dto.RequestMoveItemDto;
import group.australia.warehouse_management_system.feature.moveItem.exception.MoveItemInsufficientStockException;
import group.australia.warehouse_management_system.feature.warehouse.exception.WarehouseNotFoundException;
import group.australia.warehouse_management_system.feature.warehouse.repository.WarehouseRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MoveItemService {

    private final WarehouseRepositoryPort warehouseRepository;
    private final LocationRepositoryAdapter locationRepositoryAdapter;
    private final ItemService itemService;

    @Transactional
    public void moveItem(RequestMoveItemDto req) {
        var warehouse = warehouseRepository.findByWarehouseNumber(req.warehouseNumber())
                .orElseThrow(() -> new WarehouseNotFoundException(req.warehouseNumber()));

        if (!warehouse.isActive()) {
            throw new BadRequestException("Warehouse " + req.warehouseNumber() + " is not active");
        }

        if (req.fromLocationCode().equals(req.toLocationCode())) {
            throw new BadRequestException("Source and destination locations cannot be the same");
        }

        var sourceLocation = locationRepositoryAdapter.findByLocationCode(warehouse.getId(), req.fromLocationCode())
                .orElseThrow(() -> new NotFoundException(
                        "Source location " + req.fromLocationCode() + " not found in warehouse " + req.warehouseNumber()));

        var destLocation = locationRepositoryAdapter.findByLocationCode(warehouse.getId(), req.toLocationCode())
                .orElseThrow(() -> new NotFoundException(
                        "Destination location " + req.toLocationCode() + " not found in warehouse " + req.warehouseNumber()));

        var sourceItem = itemService.getByItemNumberAndLocationId(req.itemNumber(), sourceLocation.getId());

        if (sourceItem.getQuantity() < req.quantity()) {
            throw new MoveItemInsufficientStockException(req.itemNumber(), sourceItem.getQuantity(), req.quantity());
        }

        sourceItem.setQuantity(sourceItem.getQuantity() - req.quantity());
        itemService.save(sourceItem);

        var destItemOpt = itemService.findByItemNumberAndLocationId(req.itemNumber(), destLocation.getId());
        if (destItemOpt.isPresent()) {
            var dest = destItemOpt.get();
            dest.setQuantity(dest.getQuantity() + req.quantity());
            itemService.save(dest);
        } else {
            itemService.save(Item.builder()
                    .itemNumber(req.itemNumber())
                    .quantity(req.quantity())
                    .location(destLocation)
                    .cartonHeader(sourceItem.getCartonHeader())
                    .build());
        }
    }
}
