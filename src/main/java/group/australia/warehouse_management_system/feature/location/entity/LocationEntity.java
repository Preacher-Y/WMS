package group.australia.warehouse_management_system.feature.location.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import group.australia.warehouse_management_system.feature.warehouse.entity.WarehouseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Locations", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "warehouse_id", "`row`", "section", "shelf" })
})
@Getter
@Setter
@NoArgsConstructor
public class LocationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "`row`")
    private Integer row;
    private Integer section;
    private Integer shelf;
    private String locationCode;
    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;
    @Version
    private Integer version;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdTimestamp;
    @UpdateTimestamp
    private LocalDateTime updatedTimestamp;
}