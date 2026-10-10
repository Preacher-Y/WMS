package group.australia.warehouse_management_system.unit.warehouse;

import group.australia.warehouse_management_system.feature.warehouse.domain.Warehouse;
import group.australia.warehouse_management_system.feature.warehouse.repository.WarehouseRepositoryPort;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class InMemoryWarehouseRepository implements WarehouseRepositoryPort {

    private final Map<String, Warehouse> store = new HashMap<>();
    private long nextId = 1;

    @Override
    public boolean existsByWarehouseNumber(String warehouseNumber) {
        return store.containsKey(warehouseNumber);
    }

    @Override
    public Optional<Warehouse> findByWarehouseNumber(String warehouseNumber) {
        return Optional.ofNullable(store.get(warehouseNumber));
    }

    @Override
    public Warehouse save(Warehouse warehouse) {
        var id = warehouse.getId() != null ? warehouse.getId() : nextId++;
        var saved = warehouse.toBuilder().id(id).build();
        store.put(saved.getWarehouseNumber(), saved);
        return saved;
    }

    @Override
    public Optional<Warehouse> findById(Long id) {
        return Optional.empty();
    }
}
