package group.australia.warehouse_management_system.feature.cartonHeader.repository;

import group.australia.warehouse_management_system.feature.cartonHeader.entity.CartonHeaderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartonHeaderRepository extends JpaRepository<CartonHeaderEntity, Long> {
    boolean existsByBarcode(String barcode);
    Optional<CartonHeaderEntity> findByBarcode(String barcode);
}
