package group.australia.warehouse_management_system.feature.location.repository;

import java.util.Optional;

import group.australia.warehouse_management_system.feature.warehouse.repository.WarehouseJpaRepository;
import org.springframework.stereotype.Component;

import group.australia.warehouse_management_system.feature.location.domain.Location;
import group.australia.warehouse_management_system.feature.location.domain.LocationCode;
import group.australia.warehouse_management_system.feature.location.mapper.LocationMapper;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LocationRepositoryAdapter implements LocationRepositoryPort {

    private final LocationRepository locationRepository;
    private final WarehouseJpaRepository warehouseRepository;
    private final LocationMapper locationMapper;

    @Override
    public Location save(Location location) {
        var entity = locationMapper.toEntity(location);
        entity.setWarehouse(warehouseRepository.getReferenceById(location.getWarehouseId()));
        return locationMapper.toDomain(locationRepository.save(entity));
    }

    @Override
    public boolean exists(Long warehouseId, Integer row, Integer section, Integer shelf) {
        return locationRepository.existsByWarehouseIdAndRowAndSectionAndShelf(
                warehouseId, row, section, shelf);
    }

    @Override
    public boolean existsByLocationCode(Long warehouseId, String locationCode) {
        var code = LocationCode.from(locationCode);
        return locationRepository.existsByWarehouseIdAndRowAndSectionAndShelf(
                warehouseId, code.row(), code.section(), code.shelf());
    }

    @Override
    public Optional<Location> findByLocationCode(Long warehouseId, String locationCode) {
        var code = LocationCode.from(locationCode);
        return locationRepository
                .findByWarehouseIdAndRowAndSectionAndShelf(warehouseId, code.row(), code.section(), code.shelf())
                .map(locationMapper::toDomain);
    }
}