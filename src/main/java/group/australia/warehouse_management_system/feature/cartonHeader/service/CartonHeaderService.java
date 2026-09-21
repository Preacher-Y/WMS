package group.australia.warehouse_management_system.feature.cartonHeader.service;

import group.australia.warehouse_management_system.feature.cartonHeader.domain.CartonHeader;
import group.australia.warehouse_management_system.feature.cartonHeader.dto.CreateCartonHeaderDto;
import group.australia.warehouse_management_system.feature.cartonHeader.dto.UpdateCartonHeaderDto;
import group.australia.warehouse_management_system.feature.cartonHeader.exception.CartonHeaderDuplicateException;
import group.australia.warehouse_management_system.feature.cartonHeader.exception.CartonHeaderNotFoundException;
import group.australia.warehouse_management_system.feature.cartonHeader.mapper.CartonHeaderMapper;
import group.australia.warehouse_management_system.feature.cartonHeader.repository.CartonHeaderRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartonHeaderService {

    private final CartonHeaderMapper cartonHeaderMapper;
    private final CartonHeaderRepositoryPort cartonHeaderRepository;

    public CartonHeader insertNewCartonHeader(CreateCartonHeaderDto req) {
        if (cartonHeaderRepository.existsByBarcode(req.barcode())) {
            throw new CartonHeaderDuplicateException(req.barcode());
        }

        CartonHeader cartonHeader = cartonHeaderMapper.toDomainFromCreate(req);

        try {
            return cartonHeaderRepository.save(cartonHeader);
        } catch (DataIntegrityViolationException ex) {
            throw new CartonHeaderDuplicateException(req.barcode());
        }
    }

    public CartonHeader updateCartonHeader(
            String barcode,
            UpdateCartonHeaderDto updateRequest
    ) {
        CartonHeader cartonHeader = cartonHeaderRepository.findByBarcode(barcode)
                .orElseThrow(() -> new CartonHeaderNotFoundException(barcode));

        CartonHeader updated = cartonHeader.updateDescription(updateRequest.description());

        try {
            return cartonHeaderRepository.save(updated);
        } catch (DataIntegrityViolationException ex) {
            throw new CartonHeaderDuplicateException(barcode);
        }
    }
}
