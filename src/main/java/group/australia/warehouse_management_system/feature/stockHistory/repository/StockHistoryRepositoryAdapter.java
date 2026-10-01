package group.australia.warehouse_management_system.feature.stockHistory.repository;

import java.util.List;

import org.springframework.stereotype.Component;

import group.australia.warehouse_management_system.feature.stockHistory.domain.StockHistory;
import group.australia.warehouse_management_system.feature.stockHistory.mapper.StockHistoryMapper;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StockHistoryRepositoryAdapter {

    private final StockHistoryRepository repository;
    private final StockHistoryMapper mapper;

    public StockHistory save(StockHistory history) {
        return mapper.toDomain(repository.save(mapper.toEntity(history)));
    }

    public List<StockHistory> findByWarehouseNumberAndItemNumber(String warehouseNumber, String itemNumber) {
        return repository
                .findByWarehouseNumberAndItemNumberOrderByCreatedTimestampDescIdDesc(warehouseNumber, itemNumber)
                .stream().map(mapper::toDomain).toList();
    }

    public List<StockHistory> findByLocation(String warehouseNumber, String itemNumber, String locationCode) {
        return repository.findByLocation(warehouseNumber, itemNumber, locationCode)
                .stream().map(mapper::toDomain).toList();
    }
}