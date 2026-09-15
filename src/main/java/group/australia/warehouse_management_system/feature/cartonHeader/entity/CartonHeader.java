package group.australia.warehouse_management_system.feature.cartonHeader.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "carton_headers")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartonHeader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String barcode;

    @Column(nullable = false)
    private String description;

    @Version
    private  Integer version;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdTimestamp;

    @UpdateTimestamp
    private LocalDateTime updatedTimestamp;
}
