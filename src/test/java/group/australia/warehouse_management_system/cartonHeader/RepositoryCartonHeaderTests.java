package group.australia.warehouse_management_system.cartonHeader;

import group.australia.warehouse_management_system.feature.cartonHeader.entity.CartonHeader;
import group.australia.warehouse_management_system.feature.cartonHeader.repository.CartonHeaderRepository;
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
class RepositoryCartonHeaderTests {

    @Autowired
    private CartonHeaderRepository cartonHeaderRepository;
    private CartonHeader savedCartonHeader;

    @BeforeEach
    void setUp(){
        savedCartonHeader = cartonHeaderRepository.save(CartonHeader.builder()
                .barcode("13431324532")
                .description("Testing CartonHeader")
                .build()
        );
    }

    @Test
    void CartonHeaderRepository_SaveAll_ReturnsCartonHeader(){
        Assertions.assertThat(savedCartonHeader).isNotNull();
        Assertions.assertThat(savedCartonHeader.getBarcode()).isEqualTo("13431324532");
        Assertions.assertThat(savedCartonHeader.getDescription()).isEqualTo("Testing CartonHeader");
        Assertions.assertThat(savedCartonHeader.getId()).isGreaterThan(0);
    }

    @Test
    void CartonHeaderRepository_FindById_ReturnsCartonHeader(){
        Optional<CartonHeader> cartonHeader = cartonHeaderRepository.findById(savedCartonHeader.getId());

        Assertions.assertThat(cartonHeader).isPresent();
        Assertions.assertThat(cartonHeader.get().getId()).isGreaterThan(0);
        Assertions.assertThat(cartonHeader.get().getBarcode()).isEqualTo("13431324532");
    }

    @Test
    void CartonHeaderRepository_ExistByBarcode_ReturnsBoolean(){
        boolean res = cartonHeaderRepository.existsByBarcode(savedCartonHeader.getBarcode());

        Assertions.assertThat(res).isTrue();
    }

    @Test
    void CartonHeaderRepository_FindByBarcode_ReturnCartonHeader(){
        Optional<CartonHeader> existCartonHeader = cartonHeaderRepository.findByBarcode(savedCartonHeader.getBarcode());

        Assertions.assertThat(existCartonHeader).isPresent();
        Assertions.assertThat(existCartonHeader.get().getBarcode()).isEqualTo(savedCartonHeader.getBarcode());
        Assertions.assertThat(existCartonHeader.get().getDescription()).isEqualTo(savedCartonHeader.getDescription());
    }

    @Test
    void CartonHeaderRepository_DuplicateBarcode_ThrowsDataIntegrityViolationException() {
        CartonHeader duplicate = CartonHeader.builder()
                .barcode("13431324532")
                .description("Another description")
                .build();

        Assertions.assertThatThrownBy(() -> cartonHeaderRepository.saveAndFlush(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void CartonHeaderRepository_StaleVersion_ThrowsOptimisticLockingFailureException() {
        Integer originalVersion = savedCartonHeader.getVersion();

        savedCartonHeader.setDescription("First Update");
        cartonHeaderRepository.saveAndFlush(savedCartonHeader);

        CartonHeader stale = CartonHeader.builder()
                .id(savedCartonHeader.getId())
                .barcode(savedCartonHeader.getBarcode())
                .description("Stale Update")
                .version(originalVersion)
                .build();

        Assertions.assertThatThrownBy(() -> cartonHeaderRepository.saveAndFlush(stale))
                .isInstanceOf(ObjectOptimisticLockingFailureException.class);
    }
}
