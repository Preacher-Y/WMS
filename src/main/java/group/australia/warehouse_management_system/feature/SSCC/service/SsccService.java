package group.australia.warehouse_management_system.feature.SSCC.service;

import group.australia.warehouse_management_system.feature.SSCC.domain.Sscc;
import group.australia.warehouse_management_system.feature.SSCC.dto.CreateSsccDto;
import group.australia.warehouse_management_system.feature.SSCC.exception.SsccCartonHeaderNotFoundException;
import group.australia.warehouse_management_system.feature.SSCC.exception.SsccDuplicateException;
import group.australia.warehouse_management_system.feature.SSCC.mapper.SsccMapper;
import group.australia.warehouse_management_system.feature.SSCC.repository.SsccRepositoryPort;
import group.australia.warehouse_management_system.feature.cartonHeader.repository.CartonHeaderRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SsccService {

    private final SsccMapper ssccMapper;
    private final SsccRepositoryPort ssccRepository;
    private final CartonHeaderRepositoryPort cartonHeaderRepository;

    public Sscc insertNewSscc(String barcode, CreateSsccDto req) {

        if (ssccRepository.existsBySscc(req.sscc())) {
            throw new SsccDuplicateException(req.sscc());
        }

        var cartonHeader = cartonHeaderRepository.findByBarcode(barcode)
                .orElseThrow(() -> new SsccCartonHeaderNotFoundException(barcode));

        var sscc = ssccMapper.toDomainFromCreate(req).withCartonHeader(cartonHeader);
        try {
            return ssccRepository.save(sscc);
        } catch (DataIntegrityViolationException ex) {
            throw new SsccDuplicateException(req.sscc());
        }
    }
}
