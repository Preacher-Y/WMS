package group.australia.warehouse_management_system.unit.cartonHeader;

import group.australia.warehouse_management_system.feature.cartonHeader.domain.CartonHeader;
import group.australia.warehouse_management_system.feature.cartonHeader.repository.CartonHeaderRepositoryPort;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class InMemoryCartonHeaderRepository implements CartonHeaderRepositoryPort {

    private final Map<String, CartonHeader> store = new HashMap<>();
    private long nextId = 1;

    @Override
    public boolean existsByBarcode(String barcode) {
        return store.containsKey(barcode);
    }

    @Override
    public Optional<CartonHeader> findByBarcode(String barcode) {
        return Optional.ofNullable(store.get(barcode));
    }

    @Override
    public Optional<CartonHeader> findById(Long id) {
        return store.values().stream().filter(c -> id.equals(c.getId())).findFirst();
    }

    @Override
    public CartonHeader save(CartonHeader cartonHeader) {
        var id = cartonHeader.getId() != null ? cartonHeader.getId() : nextId++;
        var saved = cartonHeader.toBuilder().id(id).build();
        store.put(saved.getBarcode(), saved);
        return saved;
    }
}
