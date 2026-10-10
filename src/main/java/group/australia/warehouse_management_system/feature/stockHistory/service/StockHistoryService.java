package group.australia.warehouse_management_system.feature.stockHistory.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import group.australia.warehouse_management_system.common.exceptions.BadRequestException;
import group.australia.warehouse_management_system.feature.stockHistory.domain.StockAction;
import group.australia.warehouse_management_system.feature.stockHistory.domain.StockHistory;
import group.australia.warehouse_management_system.feature.stockHistory.dto.StockHistoryResponse;
import group.australia.warehouse_management_system.feature.stockHistory.repository.StockHistoryRepositoryAdapter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockHistoryService {

    private final StockHistoryRepositoryAdapter history;

    @Transactional(propagation = Propagation.MANDATORY)
    public void record(StockAction action, String itemNumber, int quantityChange,
                       String fromLocationCode, String toLocationCode,
                       String warehouseNumber, String sscc) {
        history.save(StockHistory.builder()
                .action(action)
                .itemNumber(itemNumber)
                .quantityChange(quantityChange)
                .fromLocationCode(fromLocationCode)
                .toLocationCode(toLocationCode)
                .warehouseNumber(warehouseNumber)
                .sscc(sscc)
                .build());
    }

    @Transactional(readOnly = true)
    public List<StockHistoryResponse> find(String warehouseNumber, String itemNumber, String locationCode) {
        String wh = toWarehouseNumber(warehouseNumber);

        List<StockHistory> rows = (locationCode == null || locationCode.isBlank())
                ? history.findByWarehouseNumberAndItemNumber(wh, itemNumber)
                : history.findByLocation(wh, itemNumber, locationCode);

        return rows.stream()
                .map(h -> new StockHistoryResponse(h.getId(), h.getAction(), h.getItemNumber(),
                        h.getQuantityChange(), h.getFromLocationCode(), h.getToLocationCode(),
                        h.getWarehouseNumber(), h.getSscc(), h.getCreatedTimestamp()))
                .toList();
    }

    private String toWarehouseNumber(String raw) {
        try {
            return String.format("WH-%03d", Integer.parseInt(raw));
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid warehouse number: " + raw);
        }
    }
}