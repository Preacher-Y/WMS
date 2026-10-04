package group.australia.warehouse_management_system.feature.cartonHeader.entity;

import group.australia.warehouse_management_system.feature.item.entity.ItemEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carton_headers")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartonHeaderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String barcode;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "cartonHeader")
    private List<ItemEntity> items = new ArrayList<>();

    @Version
    private Integer version;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdTimestamp;

    @UpdateTimestamp
    private LocalDateTime updatedTimestamp;
}
