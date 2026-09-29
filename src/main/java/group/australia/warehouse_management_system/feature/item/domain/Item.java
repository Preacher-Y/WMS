package group.australia.warehouse_management_system.feature.item.domain;

import group.australia.warehouse_management_system.feature.cartonHeader.domain.CartonHeader;
import group.australia.warehouse_management_system.feature.location.domain.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Item {
    private Long id;
    private String itemNumber;
    private Integer quantity;
    private Location location;
    private CartonHeader cartonHeader;
    private Integer version;
    private LocalDateTime creationTimestamp;
    private LocalDateTime updateTimestamp;

    public Item withLocation(Location location) {
        return this.toBuilder().location(location).build();
    }

    public Item withCartonHeader(CartonHeader cartonHeader) {
        return this.toBuilder().cartonHeader(cartonHeader).build();
    }
}
