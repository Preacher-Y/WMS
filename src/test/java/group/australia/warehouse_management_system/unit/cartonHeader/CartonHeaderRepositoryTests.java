package group.australia.warehouse_management_system.unit.cartonHeader;

import group.australia.warehouse_management_system.feature.cartonHeader.entity.CartonHeaderEntity;
import group.australia.warehouse_management_system.feature.cartonHeader.repository.CartonHeaderJpaRepository;
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
class CartonHeaderRepositoryTests {

    @Autowired
    private CartonHeaderJpaRepository cartonHeaderJpaRepository;
    private CartonHeaderEntity savedCartonHeader;

    @BeforeEach
    void setUp() {
        savedCartonHeader = cartonHeaderJpaRepository.save(CartonHeaderEntity.builder()
                .barcode("13431324532")
                .description("Testing CartonHeader")
                .build()
        );
    }

    @Test
    void Should_save_and_return_carton_header() {
        Assertions.assertThat(savedCartonHeader).isNotNull();
        Assertions.assertThat(savedCartonHeader.getBarcode()).isEqualTo("13431324532");
        Assertions.assertThat(savedCartonHeader.getDescription()).isEqualTo("Testing CartonHeader");
        Assertions.assertThat(savedCartonHeader.getId()).isGreaterThan(0);
    }

    @Test
    void Should_find_carton_header_by_id() {
        Optional<CartonHeaderEntity> cartonHeader = cartonHeaderJpaRepository.findById(savedCartonHeader.getId());

        Assertions.assertThat(cartonHeader).isPresent();
        Assertions.assertThat(cartonHeader.get().getId()).isGreaterThan(0);
        Assertions.assertThat(cartonHeader.get().getBarcode()).isEqualTo("13431324532");
    }

    @Test
    void Should_return_true_when_barcode_exists() {
        boolean res = cartonHeaderJpaRepository.existsByBarcode(savedCartonHeader.getBarcode());

        Assertions.assertThat(res).isTrue();
    }

    @Test
    void Should_find_carton_header_by_barcode() {
        Optional<CartonHeaderEntity> existCartonHeader = cartonHeaderJpaRepository.findByBarcode(savedCartonHeader.getBarcode());

        Assertions.assertThat(existCartonHeader).isPresent();
        Assertions.assertThat(existCartonHeader.get().getBarcode()).isEqualTo(savedCartonHeader.getBarcode());
        Assertions.assertThat(existCartonHeader.get().getDescription()).isEqualTo(savedCartonHeader.getDescription());
    }

    @Test
    void Should_throw_on_duplicate_barcode() {
        CartonHeaderEntity duplicate = CartonHeaderEntity.builder()
                .barcode("13431324532")
                .description("Another description")
                .build();

        Assertions.assertThatThrownBy(() -> cartonHeaderJpaRepository.saveAndFlush(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void Should_throw_on_optimistic_locking_conflict() {
        Integer originalVersion = savedCartonHeader.getVersion();

        savedCartonHeader.setDescription("First Update");
        cartonHeaderJpaRepository.saveAndFlush(savedCartonHeader);

        CartonHeaderEntity stale = CartonHeaderEntity.builder()
                .id(savedCartonHeader.getId())
                .barcode(savedCartonHeader.getBarcode())
                .description("Stale Update")
                .version(originalVersion)
                .build();

        Assertions.assertThatThrownBy(() -> cartonHeaderJpaRepository.saveAndFlush(stale))
                .isInstanceOf(ObjectOptimisticLockingFailureException.class);
    }
}
