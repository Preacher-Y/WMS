package group.australia.warehouse_management_system.feature.inventory.dto;
import java.time.LocalDateTime;
public record InventoryItemDetailResponse(
        Long id,
        String itemNumber,
        String locationCode,
        int quantity,
        CartonHeaderInfo cartonHeader,
        LocalDateTime creationTimestamp,
        LocalDateTime updateTimestamp
) {
    public record CartonHeaderInfo(Long id, String barcode, String description) {}
}
