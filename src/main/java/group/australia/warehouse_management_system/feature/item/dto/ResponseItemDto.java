package group.australia.warehouse_management_system.feature.item.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record ResponseItemDto(Long id, String itemNumber, Integer quantity,
                              Integer locationRow, Integer locationSection, Integer locationShelf,
                              String locationLocationCode, String cartonHeaderBarcode, String cartonHeaderDescription,
                              Integer version) implements Serializable {
}