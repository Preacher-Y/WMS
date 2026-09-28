package group.australia.warehouse_management_system.feature.user.repository;

import group.australia.warehouse_management_system.feature.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity,Long> {

    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);

}
