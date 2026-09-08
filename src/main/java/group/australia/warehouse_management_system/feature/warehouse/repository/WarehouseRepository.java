package group.australia.warehouse_management_system.feature.warehouse.repository;

import group.australia.warehouse_management_system.feature.warehouse.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
}
