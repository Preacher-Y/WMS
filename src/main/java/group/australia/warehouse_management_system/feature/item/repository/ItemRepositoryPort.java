package group.australia.warehouse_management_system.feature.item.repository;

import group.australia.warehouse_management_system.feature.item.domain.Item;

import java.util.Optional;

public interface ItemRepositoryPort {
    boolean existsByItemNumberAndLocationId(String itemNumber, Long locationId);
    Optional<Item> findByItemNumberAndLocationId(String itemNumber, Long locationId);
    Item save(Item item);
}
