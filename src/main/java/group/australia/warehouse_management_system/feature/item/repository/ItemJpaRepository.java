package group.australia.warehouse_management_system.feature.item.repository;

import group.australia.warehouse_management_system.feature.item.entity.ItemEntity;
import group.australia.warehouse_management_system.feature.location.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ItemJpaRepository extends JpaRepository<ItemEntity, Long> {
    boolean existsByItemNumberAndLocationId(String itemNumber, Long locationId);
    boolean existsByItemNumber(String itemNumber);
    Optional<ItemEntity> findByItemNumberAndLocation(String itemNumber, LocationEntity location);
    Optional<ItemEntity> findByItemNumberAndLocationId(String itemNumber, Long locationId);
    List<ItemEntity> findAllByLocation_WarehouseId(Long warehouseId);
}
