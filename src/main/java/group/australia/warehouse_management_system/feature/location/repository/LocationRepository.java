package group.australia.warehouse_management_system.feature.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import group.australia.warehouse_management_system.feature.location.entity.LocationEntity;

public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
    boolean existsByWarehouseIdAndRowAndSectionAndShelf(
        Long warehouseId,
        Integer row,
        Integer section,
        Integer shelf);
}
