package group.australia.warehouse_management_system.feature.item.entity;

import group.australia.warehouse_management_system.feature.cartonHeader.entity.CartonHeaderEntity;
import group.australia.warehouse_management_system.feature.location.entity.LocationEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "items", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"item_number", "location_id"})
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "item_number")
    private String itemNumber;

    @Column(nullable = false)
    @Min(value = 0, message = "Quantity need to be a positive number")
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private LocationEntity location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carton_header_id", nullable = false)
    private CartonHeaderEntity cartonHeader;

    @Version
    private Integer version;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime creationTimestamp;

    @UpdateTimestamp
    private LocalDateTime updateTimestamp;
}
