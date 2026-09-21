package group.australia.warehouse_management_system.feature.SSCC.domain;

import group.australia.warehouse_management_system.feature.cartonHeader.domain.CartonHeader;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Sscc {
    private Long id;
    private String sscc;
    private CartonHeader cartonHeader;
    private LocalDateTime receivedTimestamp;
    private LocalDateTime createdTimeStamp;
    private LocalDateTime updatedTimeStamp;
}
