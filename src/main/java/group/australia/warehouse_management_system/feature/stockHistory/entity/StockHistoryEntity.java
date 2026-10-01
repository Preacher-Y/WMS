package group.australia.warehouse_management_system.feature.stockHistory.entity;

import java.time.LocalDateTime;

import group.australia.warehouse_management_system.feature.stockHistory.domain.StockAction;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stock_history", indexes = {
        @Index(name = "idx_history_wh_item", columnList = "warehouseNumber, itemNumber")
})
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StockHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StockAction action;

    @Column(nullable = false)
    private String itemNumber;

    @Column(nullable = false)
    private int quantityChange;

    private String fromLocationCode;
    private String toLocationCode;

    @Column(nullable = false)
    private String warehouseNumber;

    private String sscc;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdTimestamp;

    @PrePersist
    void onCreate() {
        createdTimestamp = LocalDateTime.now();
    }
}
