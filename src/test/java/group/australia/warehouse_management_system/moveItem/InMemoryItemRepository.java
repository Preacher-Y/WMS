package group.australia.warehouse_management_system.moveItem;

import group.australia.warehouse_management_system.feature.item.domain.Item;
import group.australia.warehouse_management_system.feature.item.repository.ItemRepositoryPort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class InMemoryItemRepository implements ItemRepositoryPort {

    private final Map<Long, Item> store = new HashMap<>();
    private long nextId = 1;

    @Override
    public boolean existsByItemNumberAndLocationId(String itemNumber, Long locationId) {
        return store.values().stream()
                .anyMatch(i -> i.getItemNumber().equals(itemNumber)
                        && i.getLocation().getId().equals(locationId));
    }

    @Override
    public Optional<Item> findByItemNumberAndLocationId(String itemNumber, Long locationId) {
        return store.values().stream()
                .filter(i -> i.getItemNumber().equals(itemNumber)
                        && i.getLocation().getId().equals(locationId))
                .findFirst();
    }

    @Override
    public List<Item> findAllByWarehouseId(Long warehouseId) {
        return List.of();
    }

    @Override
    public Item save(Item item) {
        var id = item.getId() != null ? item.getId() : nextId++;
        if (item.getId() != null && item.getId() >= nextId) {
            nextId = item.getId() + 1;
        }
        var saved = item.toBuilder().id(id).build();
        store.put(id, saved);
        return saved;
    }
}
