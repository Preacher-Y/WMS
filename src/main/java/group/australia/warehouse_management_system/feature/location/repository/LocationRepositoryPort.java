package group.australia.warehouse_management_system.feature.location.repository;

import group.australia.warehouse_management_system.feature.location.domain.Location;

import java.util.Optional;

public interface LocationRepositoryPort {
    Location save(Location location);
    boolean exists(Long warehouseId, Integer row, Integer section, Integer shelf);
    boolean existsByLocationCode(Long warehouseId, String locationCode);
    Optional<Location> findByLocationCode(Long warehouseId, String locationCode);

}
