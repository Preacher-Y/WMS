package group.australia.warehouse_management_system.feature.item.repository;

import group.australia.warehouse_management_system.feature.item.domain.Item;

public interface ItemRepositoryPort {
    boolean existsByItemNumberAndLocationId(String itemNumber, Long locationId);
    Item save(Item item);
}
