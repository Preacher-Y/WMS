package group.australia.warehouse_management_system.feature.SSCC.repository;

import group.australia.warehouse_management_system.feature.SSCC.domain.Sscc;

import java.util.Optional;

public interface SsccRepositoryPort {
    boolean existsBySscc(String sscc);
    Optional<Sscc> findBySscc(String sscc);
    Sscc save(Sscc sscc);
}
