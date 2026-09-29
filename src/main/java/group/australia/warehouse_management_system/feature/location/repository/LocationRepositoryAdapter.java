package group.australia.warehouse_management_system.feature.location.repository;

import org.springframework.stereotype.Component;

import group.australia.warehouse_management_system.feature.location.domain.Location;
import group.australia.warehouse_management_system.feature.location.mapper.LocationMapper;
import group.australia.warehouse_management_system.feature.warehouse.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LocationRepositoryAdapter {

    private final LocationRepository locationRepository;
    private final WarehouseRepository warehouseRepository;
    private final LocationMapper locationMapper;

    public Location save(Location location) {
        var entity = locationMapper.toEntity(location);
        entity.setWarehouse(warehouseRepository.getReferenceById(location.getWarehouseId()));
        return locationMapper.toDomain(locationRepository.save(entity));
    }

    public boolean exists(Long warehouseId, Integer row, Integer section, Integer shelf) {
        return locationRepository.existsByWarehouseIdAndRowAndSectionAndShelf(
                warehouseId, row, section, shelf);
    }
}