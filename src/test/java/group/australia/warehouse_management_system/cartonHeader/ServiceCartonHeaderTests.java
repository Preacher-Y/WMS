package group.australia.warehouse_management_system.cartonHeader;

import group.australia.warehouse_management_system.feature.cartonHeader.domain.CartonHeader;
import group.australia.warehouse_management_system.feature.cartonHeader.dto.CreateCartonHeaderDto;
import group.australia.warehouse_management_system.feature.cartonHeader.dto.UpdateCartonHeaderDto;
import group.australia.warehouse_management_system.feature.cartonHeader.exception.CartonHeaderDuplicateException;
import group.australia.warehouse_management_system.feature.cartonHeader.exception.CartonHeaderNotFoundException;
import group.australia.warehouse_management_system.feature.cartonHeader.mapper.CartonHeaderMapper;
import group.australia.warehouse_management_system.feature.cartonHeader.service.CartonHeaderService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceCartonHeaderTests {

    private InMemoryCartonHeaderRepository cartonHeaderRepository;

    @Mock
    private CartonHeaderMapper cartonHeaderMapper;

    private CartonHeaderService cartonHeaderService;

    @BeforeEach
    void setUp() {
        cartonHeaderRepository = new InMemoryCartonHeaderRepository();
        cartonHeaderService = new CartonHeaderService(cartonHeaderMapper, cartonHeaderRepository);
    }

    @Test
    void Should_create_carton_header_if_barcode_does_not_exist_yet() {
        CreateCartonHeaderDto req = CreateCartonHeaderDto.builder()
                .barcode("123456789")
                .description("Test description")
                .build();

        when(cartonHeaderMapper.toDomainFromCreate(any(CreateCartonHeaderDto.class)))
                .thenReturn(CartonHeader.builder()
                        .barcode("123456789")
                        .description("Test description")
                        .build());

        CartonHeader result = cartonHeaderService.insertNewCartonHeader(req);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getBarcode()).isEqualTo("123456789");
        Assertions.assertThat(result.getDescription()).isEqualTo("Test description");
        Assertions.assertThat(result.getId()).isGreaterThan(0);
    }

    @Test
    void Should_update_description_when_carton_header_exists() {
        cartonHeaderRepository.save(CartonHeader.builder()
                .barcode("123456789")
                .description("Test description")
                .build());

        UpdateCartonHeaderDto update = UpdateCartonHeaderDto.builder()
                .description("Test description - Updated")
                .build();

        CartonHeader result = cartonHeaderService.updateCartonHeader("123456789", update);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getBarcode()).isEqualTo("123456789");
        Assertions.assertThat(result.getDescription()).isEqualTo("Test description - Updated");
    }

    @Test
    void Should_throw_when_barcode_already_exists() {
        cartonHeaderRepository.save(CartonHeader.builder()
                .barcode("123456789")
                .description("Test description")
                .build());

        CreateCartonHeaderDto req = CreateCartonHeaderDto.builder()
                .barcode("123456789")
                .description("Another description")
                .build();

        Assertions.assertThatThrownBy(() -> cartonHeaderService.insertNewCartonHeader(req))
                .isInstanceOf(CartonHeaderDuplicateException.class);
    }

    @Test
    void Should_throw_when_updating_non_existent_carton_header() {
        UpdateCartonHeaderDto update = UpdateCartonHeaderDto.builder()
                .description("Any description")
                .build();

        Assertions.assertThatThrownBy(() -> cartonHeaderService.updateCartonHeader("UNKNOWN-999", update))
                .isInstanceOf(CartonHeaderNotFoundException.class);
    }
}
