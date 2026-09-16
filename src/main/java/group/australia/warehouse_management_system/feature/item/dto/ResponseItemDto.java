package group.australia.warehouse_management_system.feature.item.dto;

import jakarta.validation.constraints.Min;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record ResponseItemDto(Long id, String itemNumber,
                              @Min(message = "Quantity need to be a positive number", value = 0) Integer quantity,
                              Integer locationRow, Integer locationSection, Integer locationShelf,
                              String locationLocationCode, String cartonHeaderBarcode, String cartonHeaderDescription,
                              Integer version) implements Serializable {
}