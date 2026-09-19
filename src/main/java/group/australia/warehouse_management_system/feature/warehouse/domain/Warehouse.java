package group.australia.warehouse_management_system.feature.warehouse.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Warehouse {

    private Long id;

    private String warehouseNumber;

    private String name;

    @Builder.Default
    private boolean active = true;

    private Integer version;

    private LocalDateTime createdTimestamp;

    private LocalDateTime updatedTimestamp;
}
