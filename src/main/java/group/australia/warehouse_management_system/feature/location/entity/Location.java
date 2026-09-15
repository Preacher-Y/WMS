package group.australia.warehouse_management_system.feature.location.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import group.australia.warehouse_management_system.feature.item.entity.Item;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import group.australia.warehouse_management_system.feature.warehouse.entity.Warehouse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Locations", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "warehouse_id", "location_code" })
}

)
@Getter
@Setter
@NoArgsConstructor
public class Location {
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

    @OneToMany(mappedBy = "location")
    private List<Item> items = new ArrayList<>();

    @Version
    private Integer version;

    @CreationTimestamp
    @Column(updatable = false, insertable = false, nullable = false)
    private LocalDateTime createdTimestamp;

    @UpdateTimestamp
    private LocalDateTime updatedTimestamp;

}
