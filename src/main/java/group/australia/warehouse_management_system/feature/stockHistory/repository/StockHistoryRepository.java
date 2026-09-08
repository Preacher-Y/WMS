package group.australia.warehouse_management_system.feature.stockHistory.repository;

import group.australia.warehouse_management_system.feature.stockHistory.entity.StockHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockHistoryRepository extends JpaRepository<StockHistory, Long> {
}
