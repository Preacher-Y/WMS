package group.australia.warehouse_management_system.feature.stockHistory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import group.australia.warehouse_management_system.feature.stockHistory.entity.StockHistoryEntity;

public interface StockHistoryRepository extends JpaRepository<StockHistoryEntity, Long> {

    List<StockHistoryEntity> findByWarehouseNumberAndItemNumberOrderByCreatedTimestampDescIdDesc(
            String warehouseNumber, String itemNumber);

    @Query("""
            select h from StockHistoryEntity h
            where h.warehouseNumber = :warehouseNumber
              and h.itemNumber = :itemNumber
              and (h.fromLocationCode = :locationCode or h.toLocationCode = :locationCode)
            order by h.createdTimestamp desc, h.id desc
            """)
    List<StockHistoryEntity> findByLocation(@Param("warehouseNumber") String warehouseNumber,
                                            @Param("itemNumber") String itemNumber,
                                            @Param("locationCode") String locationCode);
}