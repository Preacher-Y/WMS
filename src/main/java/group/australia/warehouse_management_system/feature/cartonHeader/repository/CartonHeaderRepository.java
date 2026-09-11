package group.australia.warehouse_management_system.feature.cartonHeader.repository;

import group.australia.warehouse_management_system.feature.cartonHeader.entity.CartonHeader;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartonHeaderRepository extends JpaRepository<CartonHeader, Long> {
    boolean existsByBarcode(String barcode);
    CartonHeader findByBarcode(String barcode);
}
