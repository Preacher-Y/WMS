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
class WarehouseRepositoryTests {

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
    void Should_save_and_return_warehouse() {
        Assertions.assertThat(savedWarehouse).isNotNull();
        Assertions.assertThat(savedWarehouse.getId()).isGreaterThan(0);
        Assertions.assertThat(savedWarehouse.getName()).isEqualTo("Test Warehouse 1");
        Assertions.assertThat(savedWarehouse.getWarehouseNumber()).isEqualTo("WH-Test001");
    }

    @Test
    void Should_find_warehouse_by_id() {
        Optional<WarehouseEntity> result = warehouseRepository.findById(savedWarehouse.getId());

        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().getId()).isEqualTo(savedWarehouse.getId());
        Assertions.assertThat(result.get().getWarehouseNumber()).isEqualTo("WH-Test001");
    }

    @Test
    void Should_return_true_when_warehouse_number_exists() {
        boolean res = warehouseRepository.existsByWarehouseNumber(savedWarehouse.getWarehouseNumber());

        Assertions.assertThat(res).isTrue();
    }

    @Test
    void Should_find_warehouse_by_warehouse_number() {
        Optional<WarehouseEntity> res = warehouseRepository.findByWarehouseNumber(savedWarehouse.getWarehouseNumber());

        Assertions.assertThat(res).isPresent();
        Assertions.assertThat(res.get().getId()).isEqualTo(savedWarehouse.getId());
        Assertions.assertThat(res.get().getWarehouseNumber()).isEqualTo("WH-Test001");

    }

    @Test
    void Should_throw_on_duplicate_warehouse_number() {
        WarehouseEntity duplicate = WarehouseEntity.builder()
                .name("Duplicate Warehouse")
                .warehouseNumber("WH-Test001")
                .active(true)
                .build();

        Assertions.assertThatThrownBy(() -> warehouseRepository.saveAndFlush(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void Should_throw_on_optimistic_locking_conflict() {
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