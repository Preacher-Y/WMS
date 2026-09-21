package group.australia.warehouse_management_system.SSCC;

import group.australia.warehouse_management_system.feature.SSCC.domain.Sscc;
import group.australia.warehouse_management_system.feature.SSCC.dto.CreateSsccDto;
import group.australia.warehouse_management_system.feature.SSCC.exception.SsccCartonHeaderNotFoundException;
import group.australia.warehouse_management_system.feature.SSCC.exception.SsccDuplicateException;
import group.australia.warehouse_management_system.feature.SSCC.mapper.SsccMapper;
import group.australia.warehouse_management_system.feature.SSCC.service.SsccService;
import group.australia.warehouse_management_system.feature.cartonHeader.domain.CartonHeader;
import group.australia.warehouse_management_system.feature.cartonHeader.repository.CartonHeaderRepositoryPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceSsccTests {

    private InMemorySsccRepository ssccRepository;

    @Mock
    private SsccMapper ssccMapper;

    @Mock
    private CartonHeaderRepositoryPort cartonHeaderRepository;

    private SsccService ssccService;

    @BeforeEach
    void setUp() {
        ssccRepository = new InMemorySsccRepository();
        ssccService = new SsccService(ssccMapper, ssccRepository, cartonHeaderRepository);
    }

    @Test
    void Should_create_sscc_if_it_does_not_exist_yet() {
        var req = CreateSsccDto.builder().sscc("1234567890123456789").build();

        var cartonHeader = CartonHeader.builder()
                .id(1L).barcode("13431324532").description("Test CartonHeader").version(0).build();

        when(cartonHeaderRepository.findByBarcode("13431324532")).thenReturn(Optional.of(cartonHeader));
        when(ssccMapper.toDomainFromCreate(Mockito.any(CreateSsccDto.class)))
                .thenReturn(Sscc.builder().sscc("1234567890123456789").build());

        var result = ssccService.insertNewSscc("13431324532", req);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getSscc()).isEqualTo("1234567890123456789");
        Assertions.assertThat(result.getId()).isGreaterThan(0);
        Assertions.assertThat(result.getCartonHeader()).isEqualTo(cartonHeader);
    }

    @Test
    void Should_associate_carton_header_when_creating_sscc() {
        var req = CreateSsccDto.builder().sscc("1234567890123456789").build();

        var cartonHeader = CartonHeader.builder()
                .id(1L).barcode("13431324532").description("Test CartonHeader").version(0).build();

        when(cartonHeaderRepository.findByBarcode("13431324532")).thenReturn(Optional.of(cartonHeader));
        when(ssccMapper.toDomainFromCreate(Mockito.any(CreateSsccDto.class)))
                .thenReturn(Sscc.builder().sscc("1234567890123456789").build());

        var result = ssccService.insertNewSscc("13431324532", req);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getCartonHeader()).isNotNull();
        Assertions.assertThat(result.getCartonHeader().getBarcode()).isEqualTo("13431324532");
    }

    @Test
    void Should_throw_when_sscc_already_exists() {
        ssccRepository.save(Sscc.builder().sscc("1234567890123456789").build());

        var req = CreateSsccDto.builder().sscc("1234567890123456789").build();

        Assertions.assertThatThrownBy(() -> ssccService.insertNewSscc("13431324532", req))
                .isInstanceOf(SsccDuplicateException.class);
    }

    @Test
    void Should_throw_when_carton_header_not_found() {
        var req = CreateSsccDto.builder().sscc("1234567890123456789").build();

        when(cartonHeaderRepository.findByBarcode("UNKNOWN-999")).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> ssccService.insertNewSscc("UNKNOWN-999", req))
                .isInstanceOf(SsccCartonHeaderNotFoundException.class);
    }
}
