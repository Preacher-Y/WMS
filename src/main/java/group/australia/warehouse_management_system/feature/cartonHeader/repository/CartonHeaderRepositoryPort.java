package group.australia.warehouse_management_system.feature.cartonHeader.repository;

import group.australia.warehouse_management_system.feature.cartonHeader.domain.CartonHeader;

import java.util.Optional;

public interface CartonHeaderRepositoryPort {
    boolean existsByBarcode(String barcode);
    Optional<CartonHeader> findByBarcode(String barcode);
    Optional<CartonHeader> findById(Long id);
    CartonHeader save(CartonHeader cartonHeader);
}
