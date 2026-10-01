package group.australia.warehouse_management_system.feature.item.repository;

import group.australia.warehouse_management_system.feature.item.domain.Item;

import java.util.Optional;
import java.util.List;

public interface ItemRepositoryPort {
    boolean existsByItemNumberAndLocationId(String itemNumber, Long locationId);
    Optional<Item> findByItemNumberAndLocationId(String itemNumber, Long locationId);
    List<Item> findAllByWarehouseId(Long warehouseId);
    Item save(Item item);
    
}
