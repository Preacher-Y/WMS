package group.australia.warehouse_management_system.feature.stockHistory.mapper;

import org.springframework.stereotype.Component;

import group.australia.warehouse_management_system.feature.stockHistory.domain.StockHistory;
import group.australia.warehouse_management_system.feature.stockHistory.entity.StockHistoryEntity;

@Component
public class StockHistoryMapper {

    public StockHistory toDomain(StockHistoryEntity e) {
        return StockHistory.builder()
                .id(e.getId())
                .action(e.getAction())
                .itemNumber(e.getItemNumber())
                .quantityChange(e.getQuantityChange())
                .fromLocationCode(e.getFromLocationCode())
                .toLocationCode(e.getToLocationCode())
                .warehouseNumber(e.getWarehouseNumber())
                .sscc(e.getSscc())
                .createdTimestamp(e.getCreatedTimestamp())
                .build();
    }

    public StockHistoryEntity toEntity(StockHistory h) {
        return StockHistoryEntity.builder()
                .action(h.getAction())
                .itemNumber(h.getItemNumber())
                .quantityChange(h.getQuantityChange())
                .fromLocationCode(h.getFromLocationCode())
                .toLocationCode(h.getToLocationCode())
                .warehouseNumber(h.getWarehouseNumber())
                .sscc(h.getSscc())
                .build();
    }
}