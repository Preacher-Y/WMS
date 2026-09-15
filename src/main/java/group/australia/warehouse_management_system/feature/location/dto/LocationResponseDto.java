package group.australia.warehouse_management_system.feature.location.dto;

import java.time.LocalDateTime;

public record LocationResponseDto( Long id,
        Integer row,
        Integer section,
        Integer shelf,
        String locationCode,
        Long warehouseId,
        Integer version,
        LocalDateTime createdTimestamp,
        LocalDateTime updatedTimestamp) {
       

}
