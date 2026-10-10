package group.australia.warehouse_management_system.feature.stockHistory.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import group.australia.warehouse_management_system.feature.stockHistory.dto.StockHistoryResponse;
import group.australia.warehouse_management_system.feature.stockHistory.service.StockHistoryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class StockHistoryController {

    private final StockHistoryService service;

    @GetMapping
    public List<StockHistoryResponse> getHistory(
            @RequestParam String warehouseNumber,
            @RequestParam String itemNumber,
            @RequestParam(required = false) String locationCode) {
        return service.find(warehouseNumber, itemNumber, locationCode);
    }
}