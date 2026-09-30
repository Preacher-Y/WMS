package group.australia.warehouse_management_system.feature.location.domain;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Location {
    private Long id;
    private Integer row;
    private Integer section;
    private Integer shelf;
    private Long warehouseId;
    private Integer version;
    private LocalDateTime createdTimestamp;
    private LocalDateTime updatedTimestamp;

    public String getLocationCode() {
        return row + "-" + section + "-" + shelf;
    }
}