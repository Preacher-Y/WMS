package group.australia.warehouse_management_system.feature.inventory.dto;
public record InventoryItemResponse(
        String itemNumber,
        String locationCode,
        int quantity
) {}