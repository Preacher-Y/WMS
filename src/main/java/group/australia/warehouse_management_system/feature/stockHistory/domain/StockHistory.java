package group.australia.warehouse_management_system.feature.stockHistory.domain;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StockHistory {
    private final Long id;
    private final StockAction action;
    private final String itemNumber;
    private final int quantityChange;
    private final String fromLocationCode;
    private final String toLocationCode;
    private final String warehouseNumber;
    private final String sscc;
    private final LocalDateTime createdTimestamp;
}