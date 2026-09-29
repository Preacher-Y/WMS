package group.australia.warehouse_management_system.user;

import group.australia.warehouse_management_system.feature.user.domain.User;
import group.australia.warehouse_management_system.feature.user.repository.UserRepositoryPort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class InMemoryUserRepository implements UserRepositoryPort {

    private final Map<Long, User> store = new HashMap<>();
    private long nextId = 1;

    @Override
    public boolean existsByEmail(String email) {
        return store.values().stream().anyMatch(u -> u.getEmail().equals(email));
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public User save(User user) {
        var id = user.getId() != null ? user.getId() : nextId++;
        var saved = user.toBuilder().id(id).build();
        store.put(id, saved);
        return saved;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(store.values());
    }
}
