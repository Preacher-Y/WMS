package group.australia.warehouse_management_system.SSCC;

import group.australia.warehouse_management_system.feature.SSCC.domain.Sscc;
import group.australia.warehouse_management_system.feature.SSCC.exception.SsccCartonHeaderNotFoundException;
import group.australia.warehouse_management_system.feature.SSCC.exception.SsccDuplicateException;
import group.australia.warehouse_management_system.feature.SSCC.dto.CreateSsccDto;
import group.australia.warehouse_management_system.feature.SSCC.dto.ResponseSsccDto;
import group.australia.warehouse_management_system.feature.SSCC.entity.SsccEntity;
import group.australia.warehouse_management_system.feature.SSCC.mapper.SsccMapper;
import group.australia.warehouse_management_system.feature.SSCC.repository.SsccRepository;
import group.australia.warehouse_management_system.feature.SSCC.service.SsccService;
import group.australia.warehouse_management_system.feature.cartonHeader.domain.CartonHeader;
import group.australia.warehouse_management_system.feature.cartonHeader.dto.ResponseCartonHeaderDto;
import group.australia.warehouse_management_system.feature.cartonHeader.repository.CartonHeaderRepositoryPort;
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
    private CartonHeaderRepositoryPort cartonHeaderRepository;

    @InjectMocks
    private SsccService ssccService;

    @Test
    void SsccService_CreateSscc_ReturnsResponseSsccDto() {
        var req = CreateSsccDto.builder().sscc("1234567890123456789").build();

        var cartonHeader = CartonHeader.builder()
                .id(1L).barcode("13431324532").description("Test CartonHeader").version(0).build();

        var ssccDomain = Sscc.builder().sscc("1234567890123456789").build();
        var ssccDomainWithCH = ssccDomain.toBuilder().cartonHeader(cartonHeader).build();
        var ssccEntity = SsccEntity.builder().id(1L).sscc("1234567890123456789").build();

        var responseDto = ResponseSsccDto.builder()
                .id(1L).sscc("1234567890123456789")
                .cartonHeader(ResponseCartonHeaderDto.builder()
                        .id(1L).barcode("13431324532").description("Test CartonHeader").version(0).build())
                .build();

        when(ssccRepository.existsBySscc("1234567890123456789")).thenReturn(false);
        when(cartonHeaderRepository.existsByBarcode("13431324532")).thenReturn(true);
        when(cartonHeaderRepository.findByBarcode("13431324532")).thenReturn(Optional.of(cartonHeader));
        when(ssccMapper.toDomainFromCreate(Mockito.any(CreateSsccDto.class))).thenReturn(ssccDomain);
        when(ssccMapper.toEntity(Mockito.any(Sscc.class))).thenReturn(ssccEntity);
        when(ssccRepository.save(Mockito.any(SsccEntity.class))).thenReturn(ssccEntity);
        when(ssccMapper.toDomain(Mockito.any(SsccEntity.class))).thenReturn(ssccDomainWithCH);
        when(ssccMapper.toDto(Mockito.any(Sscc.class))).thenReturn(responseDto);

        var result = ssccService.insertNewSscc("13431324532", req);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.sscc()).isEqualTo("1234567890123456789");
        Assertions.assertThat(result.id()).isGreaterThan(0);
    }

    @Test
    void SsccService_CreateSscc_CorrectCartonHeaderAssociation() {
        var req = CreateSsccDto.builder().sscc("1234567890123456789").build();

        var cartonHeader = CartonHeader.builder()
                .id(1L).barcode("13431324532").description("Test CartonHeader").version(0).build();

        var ssccDomain = Sscc.builder().sscc("1234567890123456789").build();
        var ssccDomainWithCH = ssccDomain.toBuilder().cartonHeader(cartonHeader).build();
        var ssccEntity = SsccEntity.builder().id(1L).sscc("1234567890123456789").build();

        var responseDto = ResponseSsccDto.builder()
                .id(1L).sscc("1234567890123456789")
                .cartonHeader(ResponseCartonHeaderDto.builder()
                        .id(1L).barcode("13431324532").description("Test CartonHeader").version(0).build())
                .build();

        when(ssccRepository.existsBySscc("1234567890123456789")).thenReturn(false);
        when(cartonHeaderRepository.existsByBarcode("13431324532")).thenReturn(true);
        when(cartonHeaderRepository.findByBarcode("13431324532")).thenReturn(Optional.of(cartonHeader));
        when(ssccMapper.toDomainFromCreate(Mockito.any(CreateSsccDto.class))).thenReturn(ssccDomain);
        when(ssccMapper.toEntity(Mockito.any(Sscc.class))).thenReturn(ssccEntity);
        when(ssccRepository.save(Mockito.any(SsccEntity.class))).thenReturn(ssccEntity);
        when(ssccMapper.toDomain(Mockito.any(SsccEntity.class))).thenReturn(ssccDomainWithCH);
        when(ssccMapper.toDto(Mockito.any(Sscc.class))).thenReturn(responseDto);

        var result = ssccService.insertNewSscc("13431324532", req);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.cartonHeader()).isNotNull();
        Assertions.assertThat(result.cartonHeader().barcode()).isEqualTo("13431324532");
    }

    @Test
    void SsccService_CreateSscc_DuplicateSscc_ThrowsConflictException() {
        var req = CreateSsccDto.builder().sscc("1234567890123456789").build();

        when(ssccRepository.existsBySscc("1234567890123456789")).thenReturn(true);

        Assertions.assertThatThrownBy(() -> ssccService.insertNewSscc("13431324532", req))
                .isInstanceOf(SsccDuplicateException.class);
    }

    @Test
    void SsccService_CreateSscc_UnknownCartonHeader_ThrowsNotFoundException() {
        var req = CreateSsccDto.builder().sscc("1234567890123456789").build();

        when(ssccRepository.existsBySscc("1234567890123456789")).thenReturn(false);
        when(cartonHeaderRepository.existsByBarcode("UNKNOWN-999")).thenReturn(false);

        Assertions.assertThatThrownBy(() -> ssccService.insertNewSscc("UNKNOWN-999", req))
                .isInstanceOf(SsccCartonHeaderNotFoundException.class);
    }
}
