package group.australia.warehouse_management_system.feature.stockHistory.dto;

import group.australia.warehouse_management_system.feature.stockHistory.domain.StockAction;
import java.time.LocalDateTime;

public record StockHistoryResponse(
        Long id,
        StockAction action,
        String itemNumber,
        int quantityChange,
        String fromLocationCode,
        String toLocationCode,
        String warehouseNumber,
        String sscc,
        LocalDateTime createdTimestamp
) {}