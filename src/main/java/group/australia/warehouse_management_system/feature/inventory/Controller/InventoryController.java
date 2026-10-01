package group.australia.warehouse_management_system.feature.inventory.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import group.australia.warehouse_management_system.feature.inventory.dto.InventoryItemDetailResponse;
import group.australia.warehouse_management_system.feature.inventory.dto.InventoryItemResponse;
import group.australia.warehouse_management_system.feature.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/warehouses/{warehouseNumber}/items")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService service;

    @GetMapping
    public List<InventoryItemResponse> list(@PathVariable String warehouseNumber) {
        return service.listItems(warehouseNumber);
    }

    @GetMapping("/{itemNumber}")
    public InventoryItemDetailResponse get(@PathVariable String warehouseNumber,
                                           @PathVariable String itemNumber,
                                           @RequestParam String locationCode) {
        return service.getItem(warehouseNumber, itemNumber, locationCode);
    }
}