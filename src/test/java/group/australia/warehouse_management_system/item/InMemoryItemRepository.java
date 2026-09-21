package group.australia.warehouse_management_system.item;

import group.australia.warehouse_management_system.feature.item.domain.Item;
import group.australia.warehouse_management_system.feature.item.repository.ItemRepositoryPort;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class InMemoryItemRepository implements ItemRepositoryPort {

    private final Map<Long, Item> store = new HashMap<>();
    private long nextId = 1;

    @Override
    public boolean existsByItemNumberAndLocationId(String itemNumber, Long locationId) {
        return store.values().stream().anyMatch(i ->
                i.getItemNumber().equals(itemNumber)
                && i.getLocation() != null
                && Objects.equals(i.getLocation().getId(), locationId));
    }

    @Override
    public Item save(Item item) {
        var id = item.getId() != null ? item.getId() : nextId++;
        var saved = item.toBuilder().id(id).build();
        store.put(id, saved);
        return saved;
    }
}
