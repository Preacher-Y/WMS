package group.australia.warehouse_management_system.SSCC;

import group.australia.warehouse_management_system.feature.SSCC.domain.Sscc;
import group.australia.warehouse_management_system.feature.SSCC.repository.SsccRepositoryPort;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class InMemorySsccRepository implements SsccRepositoryPort {
    private final Map<String, Sscc> store = new HashMap<>();
    private long nextId = 1;

    @Override
    public boolean existsBySscc(String sscc) {
        return store.containsKey(sscc);
    }

    @Override
    public Optional<Sscc> findBySscc(String sscc) {
        return Optional.ofNullable(store.get(sscc));
    }

    @Override
    public Sscc save(Sscc sscc) {
        var id = sscc.getId() != null ? sscc.getId() : nextId++;
        var saved = sscc.toBuilder().id(id).build();
        store.put(saved.getSscc(), saved);
        return saved;
    }
}
