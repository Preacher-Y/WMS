package group.australia.warehouse_management_system.feature.cartonHeader.service;

import group.australia.warehouse_management_system.common.exceptions.ConflictException;
import group.australia.warehouse_management_system.common.exceptions.NotFoundException;
import group.australia.warehouse_management_system.feature.cartonHeader.domain.CartonHeader;
import group.australia.warehouse_management_system.feature.cartonHeader.dto.CreateCartonHeaderDto;
import group.australia.warehouse_management_system.feature.cartonHeader.dto.ResponseCartonHeaderDto;
import group.australia.warehouse_management_system.feature.cartonHeader.dto.UpdateCartonHeaderDto;
import group.australia.warehouse_management_system.feature.cartonHeader.entity.CartonHeaderEntity;
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

    public ResponseCartonHeaderDto insertNewCartonHeader(CreateCartonHeaderDto req) {
        if (cartonHeaderRepository.existsByBarcode(req.barcode())) {
            throw new ConflictException("Carton Header barcode already exists: " + req.barcode());
        }

        CartonHeader cartonHeader = cartonHeaderMapper.toDomainFromCreate(req);
        CartonHeaderEntity entity = cartonHeaderMapper.toEntity(cartonHeader);

        try {
            CartonHeaderEntity saved = cartonHeaderRepository.save(entity);
            return cartonHeaderMapper.toDto(cartonHeaderMapper.toDomain(saved));
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException("Carton Header must have a unique data");
        }
    }

    public ResponseCartonHeaderDto updateCartonHeader(
            String barcode,
            UpdateCartonHeaderDto updateRequest
    ) {
        Optional<CartonHeaderEntity> entityOpt = cartonHeaderRepository.findByBarcode(barcode);

        if (entityOpt.isEmpty()) {
            throw new NotFoundException("Carton Header not found");
        }

        CartonHeader domain = cartonHeaderMapper.toDomain(entityOpt.get());
        CartonHeader updated = domain.toBuilder().description(updateRequest.description()).build();
        CartonHeaderEntity entity = cartonHeaderMapper.toEntity(updated);

        try {
            CartonHeaderEntity saved = cartonHeaderRepository.save(entity);
            return cartonHeaderMapper.toDto(cartonHeaderMapper.toDomain(saved));
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException("Carton Header update conflicts with existing data");
        }
    }
}
