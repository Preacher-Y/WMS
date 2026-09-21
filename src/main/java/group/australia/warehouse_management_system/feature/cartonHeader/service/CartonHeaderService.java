package group.australia.warehouse_management_system.feature.cartonHeader.service;

import group.australia.warehouse_management_system.feature.cartonHeader.domain.CartonHeader;
import group.australia.warehouse_management_system.feature.cartonHeader.dto.CreateCartonHeaderDto;
import group.australia.warehouse_management_system.feature.cartonHeader.dto.UpdateCartonHeaderDto;
import group.australia.warehouse_management_system.feature.cartonHeader.entity.CartonHeaderEntity;
import group.australia.warehouse_management_system.feature.cartonHeader.exception.CartonHeaderDuplicateException;
import group.australia.warehouse_management_system.feature.cartonHeader.exception.CartonHeaderNotFoundException;
import group.australia.warehouse_management_system.feature.cartonHeader.mapper.CartonHeaderMapper;
import group.australia.warehouse_management_system.feature.cartonHeader.repository.CartonHeaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartonHeaderService {

    private final CartonHeaderRepository cartonHeaderRepository;
    private final CartonHeaderMapper cartonHeaderMapper;

    public CartonHeader insertNewCartonHeader(CreateCartonHeaderDto req) {
        if (cartonHeaderRepository.existsByBarcode(req.barcode())) {
            throw new CartonHeaderDuplicateException(req.barcode());
        }

        CartonHeader cartonHeader = cartonHeaderMapper.toDomainFromCreate(req);
        CartonHeaderEntity entity = cartonHeaderMapper.toEntity(cartonHeader);

        try {
            CartonHeaderEntity saved = cartonHeaderRepository.save(entity);
            return cartonHeaderMapper.toDomain(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new CartonHeaderDuplicateException(req.barcode());
        }
    }

    public CartonHeader updateCartonHeader(
            String barcode,
            UpdateCartonHeaderDto updateRequest
    ) {
        Optional<CartonHeaderEntity> entityOpt = cartonHeaderRepository.findByBarcode(barcode);

        if (entityOpt.isEmpty()) {
            throw new CartonHeaderNotFoundException(barcode);
        }

        CartonHeader domain = cartonHeaderMapper.toDomain(entityOpt.get());
        CartonHeader updated = domain.updateDescription(updateRequest.description());
        CartonHeaderEntity entity = cartonHeaderMapper.toEntity(updated);

        try {
            CartonHeaderEntity saved = cartonHeaderRepository.save(entity);
            return cartonHeaderMapper.toDomain(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new CartonHeaderDuplicateException(barcode);
        }
    }
}
