package group.australia.warehouse_management_system.feature.warehouse.repository;

import group.australia.warehouse_management_system.feature.warehouse.entity.WarehouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<WarehouseEntity, Long> {
    boolean existsByWarehouseNumber(String warehouseNumber);
    Optional<WarehouseEntity> findByWarehouseNumber(String warehouseNumber);
}
