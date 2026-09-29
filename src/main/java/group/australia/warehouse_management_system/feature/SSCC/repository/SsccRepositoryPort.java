package group.australia.warehouse_management_system.feature.SSCC.repository;

import group.australia.warehouse_management_system.feature.SSCC.domain.Sscc;

public interface SsccRepositoryPort {
    boolean existsBySscc(String sscc);
    Sscc save(Sscc sscc);
}
