package group.australia.warehouse_management_system.warehouse;

import group.australia.warehouse_management_system.feature.warehouse.entity.WarehouseEntity;
import group.australia.warehouse_management_system.feature.warehouse.repository.WarehouseJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class RepositoryWarehouseTests {

    @Autowired
    private WarehouseJpaRepository warehouseRepository;
    private WarehouseEntity savedWarehouse;

    @BeforeEach
    void setUp() {
        savedWarehouse = warehouseRepository.save(WarehouseEntity.builder()
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
        Optional<WarehouseEntity> result = warehouseRepository.findById(savedWarehouse.getId());

        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().getId()).isEqualTo(savedWarehouse.getId());
        Assertions.assertThat(result.get().getWarehouseNumber()).isEqualTo("WH-Test001");
    }

    @Test
    void WarehouseRepository_ExistsByWarehouseNumber_ReturnTrue() {
        boolean res = warehouseRepository.existsByWarehouseNumber(savedWarehouse.getWarehouseNumber());

        Assertions.assertThat(res).isTrue();
    }

    @Test
    void WarehouseRepository_FindByWarehouseNumber_ReturnWarehouse() {
        Optional<WarehouseEntity> res = warehouseRepository.findByWarehouseNumber(savedWarehouse.getWarehouseNumber());

        Assertions.assertThat(res).isPresent();
        Assertions.assertThat(res.get().getId()).isEqualTo(savedWarehouse.getId());
        Assertions.assertThat(res.get().getWarehouseNumber()).isEqualTo("WH-Test001");

    }

    @Test
    void WarehouseRepository_DuplicateWarehouseNumber_ThrowsDataIntegrityViolationException() {
        WarehouseEntity duplicate = WarehouseEntity.builder()
                .name("Duplicate Warehouse")
                .warehouseNumber("WH-Test001")
                .active(true)
                .build();

        Assertions.assertThatThrownBy(() -> warehouseRepository.saveAndFlush(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void WarehouseRepository_StaleVersion_ThrowsOptimisticLockingFailureException() {
        Integer originalVersion = savedWarehouse.getVersion();

        savedWarehouse.setName("First Update");
        warehouseRepository.saveAndFlush(savedWarehouse);

        WarehouseEntity stale = WarehouseEntity.builder()
                .id(savedWarehouse.getId())
                .name("Stale Update")
                .warehouseNumber(savedWarehouse.getWarehouseNumber())
                .active(savedWarehouse.isActive())
                .version(originalVersion)
                .build();

        Assertions.assertThatThrownBy(() -> warehouseRepository.saveAndFlush(stale))
                .isInstanceOf(ObjectOptimisticLockingFailureException.class);
    }

}