package group.australia.warehouse_management_system.feature.cartonHeader.repository;

import group.australia.warehouse_management_system.feature.cartonHeader.entity.CartonHeader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartonHeaderRepository extends JpaRepository<CartonHeader, Long> {
    boolean existsByBarcode(String barcode);
    Optional<CartonHeader> findByBarcode(String barcode);
}
