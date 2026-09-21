package group.australia.warehouse_management_system.cartonHeader;

import group.australia.warehouse_management_system.common.exceptions.ConflictException;
import group.australia.warehouse_management_system.common.exceptions.NotFoundException;
import group.australia.warehouse_management_system.feature.cartonHeader.dto.CreateCartonHeaderDto;
import group.australia.warehouse_management_system.feature.cartonHeader.dto.ResponseCartonHeaderDto;
import group.australia.warehouse_management_system.feature.cartonHeader.dto.UpdateCartonHeaderDto;
import group.australia.warehouse_management_system.feature.cartonHeader.entity.CartonHeader;
import group.australia.warehouse_management_system.feature.cartonHeader.mapper.CartonHeaderMapper;
import group.australia.warehouse_management_system.feature.cartonHeader.repository.CartonHeaderRepository;
import group.australia.warehouse_management_system.feature.cartonHeader.service.CartonHeaderService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceCartonHeaderTests {

    @Mock
    private CartonHeaderRepository cartonHeaderRepository;

    @Mock
    private CartonHeaderMapper cartonHeaderMapper;

    @InjectMocks
    private CartonHeaderService cartonHeaderService;

    @Test
    void CartonHeaderService_CreateCartonHeader_ReturnsResponseCartonDto(){
        CreateCartonHeaderDto req = CreateCartonHeaderDto.builder()
                .barcode("123456789")
                .description("Test description")
                .build();

        CartonHeader entity = CartonHeader.builder()
                .id(1L)
                .barcode("123456789")
                .description("Test description")
                .version(0)
                .build();

        ResponseCartonHeaderDto res = ResponseCartonHeaderDto.builder()
                .id(1L)
                .barcode("123456789")
                .description("Test description")
                .version(0)
                .build();

        when(cartonHeaderMapper.toEntity(Mockito.any(CreateCartonHeaderDto.class))).thenReturn(entity);
        when(cartonHeaderRepository.save(Mockito.any(CartonHeader.class))).thenReturn(entity);
        when(cartonHeaderMapper.toDto(Mockito.any(CartonHeader.class))).thenReturn(res);

        ResponseCartonHeaderDto result = cartonHeaderService.insertNewCartonHeader(req);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.barcode()).isEqualTo("123456789");
        Assertions.assertThat(result.description()).isEqualTo("Test description");
        Assertions.assertThat(result.version()).isZero();
        Assertions.assertThat(result.id()).isGreaterThan(0);
    }

    @Test
    void CartonHeaderService_UpdateCartonHeader_ReturnsResponseCartonDto(){
        UpdateCartonHeaderDto update = UpdateCartonHeaderDto.builder()
                .description("Test description - Updated")
                .build();

        CartonHeader entity = CartonHeader.builder()
                .id(1L)
                .barcode("123456789")
                .description("Test description")
                .version(0)
                .build();

        when(cartonHeaderRepository.findByBarcode(Mockito.anyString())).thenReturn(Optional.ofNullable(entity));
        when(cartonHeaderRepository.save(Mockito.any(CartonHeader.class))).thenAnswer(
                invocation ->  invocation.getArgument(0)
        );
        when(cartonHeaderMapper.toDto(Mockito.any(CartonHeader.class))).thenAnswer(
                invocation ->{
                    CartonHeader cartonHeader = invocation.getArgument(0);

                    return ResponseCartonHeaderDto.builder()
                            .id(cartonHeader.getId())
                            .barcode(cartonHeader.getBarcode())
                            .description(update.description())
                            .version(cartonHeader.getVersion()+1)
                            .build();
                }
        );


        ResponseCartonHeaderDto result = cartonHeaderService.updateCartonHeader("123456789", update);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.barcode()).isEqualTo("123456789");
        Assertions.assertThat(result.description()).isEqualTo("Test description - Updated");
        Assertions.assertThat(result.version()).isGreaterThan(0);
    }

    @Test
    void CartonHeaderService_CreateCartonHeader_DuplicateBarcode_ThrowsConflictException() {
        CreateCartonHeaderDto req = CreateCartonHeaderDto.builder()
                .barcode("123456789")
                .description("Test description")
                .build();

        when(cartonHeaderRepository.existsByBarcode("123456789")).thenReturn(true);

        Assertions.assertThatThrownBy(() -> cartonHeaderService.insertNewCartonHeader(req))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void CartonHeaderService_UpdateCartonHeader_UnknownBarcode_ThrowsNotFoundException() {
        UpdateCartonHeaderDto update = UpdateCartonHeaderDto.builder()
                .description("Any description")
                .build();

        when(cartonHeaderRepository.findByBarcode("UNKNOWN-999")).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> cartonHeaderService.updateCartonHeader("UNKNOWN-999", update))
                .isInstanceOf(NotFoundException.class);
    }
}
