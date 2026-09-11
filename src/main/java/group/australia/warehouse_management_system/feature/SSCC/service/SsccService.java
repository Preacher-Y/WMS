package group.australia.warehouse_management_system.feature.SSCC.service;

import group.australia.warehouse_management_system.common.exceptions.ConflictException;
import group.australia.warehouse_management_system.common.exceptions.NotFoundException;
import group.australia.warehouse_management_system.feature.SSCC.dto.RequestSsccDto;
import group.australia.warehouse_management_system.feature.SSCC.dto.ResponseSsccDto;
import group.australia.warehouse_management_system.feature.SSCC.entity.SSCC;
import group.australia.warehouse_management_system.feature.SSCC.mapper.SsccMapper;
import group.australia.warehouse_management_system.feature.SSCC.repository.SsccRepository;
import group.australia.warehouse_management_system.feature.cartonHeader.entity.CartonHeader;
import group.australia.warehouse_management_system.feature.cartonHeader.repository.CartonHeaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SsccService {

    private final SsccRepository ssccRepository;
    private final SsccMapper ssccMapper;
    private final CartonHeaderRepository cartonHeaderRepository;

    public ResponseSsccDto insertNewSscc(String barcode,RequestSsccDto req){

        if (ssccRepository.existsBySscc(req.sscc())){
            throw new ConflictException(
                    "Serial Shipping Container Code already exist"
            );
        }

        if (!cartonHeaderRepository.existsByBarcode(barcode)){
            throw new NotFoundException(
                    "Carton header not found"
            );
        }

        CartonHeader cartonHeader = cartonHeaderRepository.findByBarcode(barcode);


        SSCC sscc = ssccMapper.toEntity(req);
        sscc.setCartonHeader(cartonHeader);

        return ssccMapper.toDto(ssccRepository.save(sscc));
    }
}
