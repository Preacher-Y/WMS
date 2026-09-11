package group.australia.warehouse_management_system.feature.SSCC.repository;

import group.australia.warehouse_management_system.feature.SSCC.entity.SSCC;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SsccRepository extends JpaRepository<SSCC, Long> {
    boolean existsBySscc(String sscc);
}
