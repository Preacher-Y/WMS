package group.australia.warehouse_management_system.feature.SSCC.domain;

import group.australia.warehouse_management_system.feature.cartonHeader.domain.CartonHeader;
import lombok.*;


import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Sscc {
    private Long id;
    private String sscc;
    private CartonHeader cartonHeader;
    private Integer version;
    private LocalDateTime receivedTimestamp;
    private LocalDateTime createdTimeStamp;
    private LocalDateTime updatedTimeStamp;

    public Sscc withCartonHeader(CartonHeader cartonHeader) {
        return this.toBuilder().cartonHeader(cartonHeader).build();
    }
}
