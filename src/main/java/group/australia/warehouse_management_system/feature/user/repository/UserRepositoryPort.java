package group.australia.warehouse_management_system.feature.user.repository;

import group.australia.warehouse_management_system.feature.user.domain.User;

import java.util.Optional;

public interface UserRepositoryPort {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    User save(User user);
}
