package group.australia.warehouse_management_system.warehouse;

import group.australia.warehouse_management_system.feature.warehouse.entity.Warehouse;
import group.australia.warehouse_management_system.feature.warehouse.repository.WarehouseRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class WarehouseRepositoryTests {

    @Autowired
    private WarehouseRepository warehouseRepository;
    private Warehouse savedWarehouse;

    @BeforeEach
    void setUp() {
        savedWarehouse = warehouseRepository.save(Warehouse.builder()
                .name("Test Warehouse 1")
                .warehouseNumber("WH-Test001")
                .active(true)
                .build());
    }

    @Test
    void WarehouseRepository_SaveAll_ReturnSavedWarehouse() {
        Assertions.assertThat(savedWarehouse).isNotNull();
        Assertions.assertThat(savedWarehouse.getId()).isGreaterThan(0);
        Assertions.assertThat(savedWarehouse.getName()).isEqualTo("Test Warehouse 1");
        Assertions.assertThat(savedWarehouse.getWarehouseNumber()).isEqualTo("WH-Test001");
    }

    @Test
    void WarehouseRepository_FindById_ReturnWarehouse() {
        Optional<Warehouse> result = warehouseRepository.findById(savedWarehouse.getId());

        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().getId()).isEqualTo(savedWarehouse.getId());
        Assertions.assertThat(result.get().getWarehouseNumber()).isEqualTo("WH-Test001");
    }

    @Test
    void WarehouseRepository_ExistsByWarehouseNumber_ReturnWarehouse() {
        boolean res = warehouseRepository.existsByWarehouseNumber(savedWarehouse.getWarehouseNumber());

        Assertions.assertThat(res).isTrue();
    }


}