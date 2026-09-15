package group.australia.warehouse_management_system.SSCC;

import group.australia.warehouse_management_system.feature.SSCC.entity.SSCC;
import group.australia.warehouse_management_system.feature.SSCC.repository.SsccRepository;
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

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class RepositorySsccTests {

    @Autowired
    private SsccRepository ssccRepository;

    @Autowired
    private CartonHeaderRepository cartonHeaderRepository;

    private SSCC savedSscc;
    private CartonHeader savedCartonHeader;

    @BeforeEach
    void setUp() {
        savedCartonHeader = cartonHeaderRepository.save(CartonHeader.builder()
                .barcode("13431324532")
                .description("Test CartonHeader")
                .build());

        savedSscc = ssccRepository.save(SSCC.builder()
                .sscc("1234567890123456789")
                .cartonHeader(savedCartonHeader)
                .build());
    }

    @Test
    void SsccRepository_SaveAll_ReturnsSscc() {
        Assertions.assertThat(savedSscc).isNotNull();
        Assertions.assertThat(savedSscc.getId()).isGreaterThan(0);
        Assertions.assertThat(savedSscc.getSscc()).isEqualTo("1234567890123456789");
        Assertions.assertThat(savedSscc.getCartonHeader()).isNotNull();
        Assertions.assertThat(savedSscc.getCartonHeader().getBarcode()).isEqualTo("13431324532");
    }

    @Test
    void SsccRepository_FindById_ReturnsSscc() {
        Optional<SSCC> result = ssccRepository.findById(savedSscc.getId());

        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().getId()).isEqualTo(savedSscc.getId());
        Assertions.assertThat(result.get().getSscc()).isEqualTo("1234567890123456789");
    }

    @Test
    void SsccRepository_ExistsBySscc_ReturnsTrue() {
        boolean result = ssccRepository.existsBySscc(savedSscc.getSscc());

        Assertions.assertThat(result).isTrue();
    }

    @Test
    void SsccRepository_DuplicateSscc_ThrowsDataIntegrityViolationException() {
        SSCC duplicate = SSCC.builder()
                .sscc("1234567890123456789")
                .cartonHeader(savedCartonHeader)
                .build();

        Assertions.assertThatThrownBy(() -> ssccRepository.saveAndFlush(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
