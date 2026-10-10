package group.australia.warehouse_management_system.feature.warehouse.repository;

import group.australia.warehouse_management_system.feature.warehouse.domain.Warehouse;

import java.util.Optional;

public interface WarehouseRepositoryPort {

    boolean existsByWarehouseNumber(String warehouseNumber);

    Optional<Warehouse> findByWarehouseNumber(String warehouseNumber);

    Warehouse save(Warehouse warehouse);

    Optional<Warehouse> findById(Long id);
}
