package group.australia.warehouse_management_system.feature.SSCC.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sscc")
@Getter
@Setter
@NoArgsConstructor
public class SSCC {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
