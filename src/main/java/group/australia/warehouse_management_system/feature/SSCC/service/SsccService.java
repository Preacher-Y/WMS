package group.australia.warehouse_management_system.feature.SSCC.service;

import group.australia.warehouse_management_system.feature.SSCC.domain.Sscc;
import group.australia.warehouse_management_system.feature.SSCC.exception.SsccCartonHeaderNotFoundException;
import group.australia.warehouse_management_system.feature.SSCC.exception.SsccDuplicateException;
import group.australia.warehouse_management_system.feature.SSCC.dto.CreateSsccDto;
import group.australia.warehouse_management_system.feature.SSCC.dto.ResponseSsccDto;
import group.australia.warehouse_management_system.feature.SSCC.entity.SsccEntity;
import group.australia.warehouse_management_system.feature.SSCC.mapper.SsccMapper;
import group.australia.warehouse_management_system.feature.SSCC.repository.SsccRepository;
import group.australia.warehouse_management_system.feature.cartonHeader.domain.CartonHeader;
import group.australia.warehouse_management_system.feature.cartonHeader.repository.CartonHeaderRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SsccService {

    private final SsccRepository ssccRepository;
    private final SsccMapper ssccMapper;
    private final CartonHeaderRepositoryPort cartonHeaderRepository;

    public ResponseSsccDto insertNewSscc(String barcode, CreateSsccDto req) {

        if (ssccRepository.existsBySscc(req.sscc())) {
            throw new SsccDuplicateException(req.sscc());
        }

        if (!cartonHeaderRepository.existsByBarcode(barcode)) {
            throw new SsccCartonHeaderNotFoundException(barcode);
        }

        CartonHeader cartonHeader = cartonHeaderRepository.findByBarcode(barcode).orElseThrow();

        Sscc sscc = ssccMapper.toDomainFromCreate(req).toBuilder().cartonHeader(cartonHeader).build();
        SsccEntity entity = ssccMapper.toEntity(sscc);
        try {
            SsccEntity saved = ssccRepository.save(entity);
            return ssccMapper.toDto(ssccMapper.toDomain(saved));
        } catch (DataIntegrityViolationException ex) {
            throw new SsccDuplicateException(req.sscc());
        }
    }
}
