package group.australia.warehouse_management_system.feature.warehouse.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
public class Warehouse {

    private Long id;

    private String warehouseNumber;

    private String name;

    private boolean active = true;

    private Integer version;

    private LocalDateTime createdTimestamp;

    private LocalDateTime updatedTimestamp;
}
