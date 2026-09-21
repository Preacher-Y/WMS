package group.australia.warehouse_management_system.feature.warehouse.repository;

import group.australia.warehouse_management_system.feature.warehouse.domain.Warehouse;
import group.australia.warehouse_management_system.feature.warehouse.mapper.WarehouseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WarehouseRepositoryAdapter implements WarehouseRepositoryPort {

    private final WarehouseJpaRepository jpaRepository;
    private final WarehouseMapper mapper;

    @Override
    public boolean existsByWarehouseNumber(String warehouseNumber) {
        return jpaRepository.existsByWarehouseNumber(warehouseNumber);
    }

    @Override
    public Optional<Warehouse> findByWarehouseNumber(String warehouseNumber) {
        return jpaRepository.findByWarehouseNumber(warehouseNumber).map(mapper::toDomain);
    }

    @Override
    public Warehouse save(Warehouse warehouse) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(warehouse)));
    }
}
