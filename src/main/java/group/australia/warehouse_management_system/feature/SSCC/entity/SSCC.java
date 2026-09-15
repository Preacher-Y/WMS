package group.australia.warehouse_management_system.feature.SSCC.entity;

import group.australia.warehouse_management_system.feature.cartonHeader.entity.CartonHeader;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "sscc")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SSCC {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String sscc;

    @OneToOne
    @JoinColumn(name = "cartonHeaderId")
    private CartonHeader cartonHeader;

    private LocalDateTime receivedTimestamp;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdTimeStamp;

    @UpdateTimestamp
    private LocalDateTime updatedTimeStamp;

}
