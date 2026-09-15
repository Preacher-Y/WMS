package group.australia.warehouse_management_system.feature.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import group.australia.warehouse_management_system.feature.location.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
    boolean existsByWarehouseIdAndLocationCode(
            Long warehouseId,
            String locationCode);
}
