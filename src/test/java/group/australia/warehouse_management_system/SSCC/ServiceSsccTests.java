package group.australia.warehouse_management_system.SSCC;

import group.australia.warehouse_management_system.common.exceptions.ConflictException;
import group.australia.warehouse_management_system.common.exceptions.NotFoundException;
import group.australia.warehouse_management_system.feature.SSCC.dto.CreateSsccDto;
import group.australia.warehouse_management_system.feature.SSCC.dto.ResponseSsccDto;
import group.australia.warehouse_management_system.feature.SSCC.entity.SSCC;
import group.australia.warehouse_management_system.feature.SSCC.mapper.SsccMapper;
import group.australia.warehouse_management_system.feature.SSCC.repository.SsccRepository;
import group.australia.warehouse_management_system.feature.SSCC.service.SsccService;
import group.australia.warehouse_management_system.feature.cartonHeader.dto.ResponseCartonHeaderDto;
import group.australia.warehouse_management_system.feature.cartonHeader.entity.CartonHeader;
import group.australia.warehouse_management_system.feature.cartonHeader.repository.CartonHeaderRepository;
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
class ServiceSsccTests {

    @Mock
    private SsccRepository ssccRepository;

    @Mock
    private SsccMapper ssccMapper;

    @Mock
    private CartonHeaderRepository cartonHeaderRepository;

    @InjectMocks
    private SsccService ssccService;

    @Test
    void SsccService_CreateSscc_ReturnsResponseSsccDto() {
        CreateSsccDto req = CreateSsccDto.builder()
                .sscc("1234567890123456789")
                .build();

        CartonHeader cartonHeader = CartonHeader.builder()
                .id(1L)
                .barcode("13431324532")
                .description("Test CartonHeader")
                .version(0)
                .build();

        SSCC sscc = SSCC.builder()
                .id(1L)
                .sscc("1234567890123456789")
                .cartonHeader(cartonHeader)
                .build();

        ResponseSsccDto responseDto = ResponseSsccDto.builder()
                .id(1L)
                .sscc("1234567890123456789")
                .cartonHeader(ResponseCartonHeaderDto.builder()
                        .id(1L)
                        .barcode("13431324532")
                        .description("Test CartonHeader")
                        .version(0)
                        .build())
                .build();

        when(ssccRepository.existsBySscc("1234567890123456789")).thenReturn(false);
        when(cartonHeaderRepository.existsByBarcode("13431324532")).thenReturn(true);
        when(cartonHeaderRepository.findByBarcode("13431324532")).thenReturn(Optional.of(cartonHeader));
        when(ssccMapper.toEntity(Mockito.any(CreateSsccDto.class))).thenReturn(sscc);
        when(ssccRepository.save(Mockito.any(SSCC.class))).thenReturn(sscc);
        when(ssccMapper.toDto(Mockito.any(SSCC.class))).thenReturn(responseDto);

        ResponseSsccDto result = ssccService.insertNewSscc("13431324532", req);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.sscc()).isEqualTo("1234567890123456789");
        Assertions.assertThat(result.id()).isGreaterThan(0);
    }

    @Test
    void SsccService_CreateSscc_CorrectCartonHeaderAssociation() {
        CreateSsccDto req = CreateSsccDto.builder()
                .sscc("1234567890123456789")
                .build();

        CartonHeader cartonHeader = CartonHeader.builder()
                .id(1L)
                .barcode("13431324532")
                .description("Test CartonHeader")
                .version(0)
                .build();

        SSCC sscc = SSCC.builder()
                .id(1L)
                .sscc("1234567890123456789")
                .cartonHeader(cartonHeader)
                .build();

        ResponseCartonHeaderDto cartonHeaderDto = ResponseCartonHeaderDto.builder()
                .id(1L)
                .barcode("13431324532")
                .description("Test CartonHeader")
                .version(0)
                .build();

        ResponseSsccDto responseDto = ResponseSsccDto.builder()
                .id(1L)
                .sscc("1234567890123456789")
                .cartonHeader(cartonHeaderDto)
                .build();

        when(ssccRepository.existsBySscc("1234567890123456789")).thenReturn(false);
        when(cartonHeaderRepository.existsByBarcode("13431324532")).thenReturn(true);
        when(cartonHeaderRepository.findByBarcode("13431324532")).thenReturn(Optional.of(cartonHeader));
        when(ssccMapper.toEntity(Mockito.any(CreateSsccDto.class))).thenReturn(sscc);
        when(ssccRepository.save(Mockito.any(SSCC.class))).thenReturn(sscc);
        when(ssccMapper.toDto(Mockito.any(SSCC.class))).thenReturn(responseDto);

        ResponseSsccDto result = ssccService.insertNewSscc("13431324532", req);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.cartonHeader()).isNotNull();
        Assertions.assertThat(result.cartonHeader().barcode()).isEqualTo("13431324532");
    }

    @Test
    void SsccService_CreateSscc_DuplicateSscc_ThrowsConflictException() {
        CreateSsccDto req = CreateSsccDto.builder()
                .sscc("1234567890123456789")
                .build();

        when(ssccRepository.existsBySscc("1234567890123456789")).thenReturn(true);

        Assertions.assertThatThrownBy(() -> ssccService.insertNewSscc("13431324532", req))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void SsccService_CreateSscc_UnknownCartonHeader_ThrowsNotFoundException() {
        CreateSsccDto req = CreateSsccDto.builder()
                .sscc("1234567890123456789")
                .build();

        when(ssccRepository.existsBySscc("1234567890123456789")).thenReturn(false);
        when(cartonHeaderRepository.existsByBarcode("UNKNOWN-999")).thenReturn(false);

        Assertions.assertThatThrownBy(() -> ssccService.insertNewSscc("UNKNOWN-999", req))
                .isInstanceOf(NotFoundException.class);
    }
}
