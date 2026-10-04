package group.australia.warehouse_management_system.feature.cartonHeader.repository;

import group.australia.warehouse_management_system.feature.cartonHeader.domain.CartonHeader;
import group.australia.warehouse_management_system.feature.cartonHeader.mapper.CartonHeaderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CartonHeaderRepositoryAdapter implements CartonHeaderRepositoryPort {

    private final CartonHeaderJpaRepository jpaRepository;
    private final CartonHeaderMapper mapper;

    @Override
    public boolean existsByBarcode(String barcode) {
        return jpaRepository.existsByBarcode(barcode);
    }

    @Override
    public Optional<CartonHeader> findByBarcode(String barcode) {
        return jpaRepository.findByBarcode(barcode).map(mapper::toDomain);
    }

    @Override
    public Optional<CartonHeader> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public CartonHeader save(CartonHeader cartonHeader) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(cartonHeader)));
    }
}
