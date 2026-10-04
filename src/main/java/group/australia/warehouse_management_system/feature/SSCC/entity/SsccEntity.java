package group.australia.warehouse_management_system.feature.SSCC.entity;

import group.australia.warehouse_management_system.feature.cartonHeader.entity.CartonHeaderEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "sscc")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SsccEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String sscc;

    @OneToOne
    @JoinColumn(name = "cartonHeaderId")
    private CartonHeaderEntity cartonHeader;

    private LocalDateTime receivedTimestamp;

    @Version
    private Integer version;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdTimeStamp;

    @UpdateTimestamp
    private LocalDateTime updatedTimeStamp;
}
