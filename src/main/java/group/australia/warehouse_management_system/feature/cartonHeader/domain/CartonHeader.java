package group.australia.warehouse_management_system.feature.cartonHeader.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CartonHeader {
    private Long id;
    private String barcode;
    private String description;
    private Integer version;
    private LocalDateTime createdTimestamp;
    private LocalDateTime updatedTimestamp;

    public CartonHeader updateDescription(String description) {
        return this.toBuilder().description(description).build();
    }
}
