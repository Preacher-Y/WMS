package group.australia.warehouse_management_system.feature.cartonHeader.service;

import group.australia.warehouse_management_system.common.exceptions.ConflictException;
import group.australia.warehouse_management_system.common.exceptions.NotFoundException;
import group.australia.warehouse_management_system.feature.cartonHeader.dto.RequestCartonHeaderDto;
import group.australia.warehouse_management_system.feature.cartonHeader.dto.ResponseCartonHeaderDto;
import group.australia.warehouse_management_system.feature.cartonHeader.dto.UpdateCartonHeaderDto;
import group.australia.warehouse_management_system.feature.cartonHeader.entity.CartonHeader;
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

    public ResponseCartonHeaderDto insertNewCartonHeader(RequestCartonHeaderDto req){
        if (cartonHeaderRepository.existsByBarcode(req.barcode())){
            throw new ConflictException(
                    "Carton Header barcode already exists: " + req.barcode()
            );
        }

        CartonHeader cartonHeader = cartonHeaderMapper.toEntity(req);

        try {
            cartonHeaderRepository.save(cartonHeader);
            return  cartonHeaderMapper.toDto(cartonHeader);
        } catch (DataIntegrityViolationException ex){
            throw  new ConflictException(
                    "Carton Header must have a unique data"
            );
        }
    }

    public ResponseCartonHeaderDto updateCartonHeader(
            String barcode,
            UpdateCartonHeaderDto updateRequest
    ) {
        Optional<CartonHeader> cartonHeader = cartonHeaderRepository.findByBarcode(barcode);

        if (cartonHeader.isEmpty()){
            throw new NotFoundException(
                    "Carton Header not found"
            );
        }

        cartonHeader.get().setDescription(updateRequest.description());

        try {
            cartonHeaderRepository.save(cartonHeader.get());
            return cartonHeaderMapper.toDto(cartonHeader.get());
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException("Carton Header update conflicts with existing data");
        }
    }
}
