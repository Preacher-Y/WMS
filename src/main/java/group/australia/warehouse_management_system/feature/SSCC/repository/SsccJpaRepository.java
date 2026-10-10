package group.australia.warehouse_management_system.feature.SSCC.repository;

import group.australia.warehouse_management_system.feature.SSCC.entity.SsccEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SsccJpaRepository extends JpaRepository<SsccEntity, Long> {
    boolean existsBySscc(String sscc);
    Optional<SsccEntity> findBySscc(String sscc);
}
