package group.australia.warehouse_management_system.feature.location.repository;

import java.util.Optional;

import org.springframework.stereotype.Component;

import group.australia.warehouse_management_system.common.exceptions.BadRequestException;
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

    public boolean existsByLocationCode(Long warehouseId, String locationCode) {
        int[] p = parseLocationCode(locationCode);
        return locationRepository.existsByWarehouseIdAndRowAndSectionAndShelf(
                warehouseId, p[0], p[1], p[2]);
    }

    public Optional<Location> findByLocationCode(Long warehouseId, String locationCode) {
        int[] p = parseLocationCode(locationCode);
        return locationRepository
                .findByWarehouseIdAndRowAndSectionAndShelf(warehouseId, p[0], p[1], p[2])
                .map(entity -> locationMapper.toDomain(entity));
    }

    private int[] parseLocationCode(String locationCode) {
        String message = "Invalid location code '" + locationCode
                + "', expected format row-section-shelf, e.g. 1-2-4";
        if (locationCode == null || !locationCode.matches("\\d+-\\d+-\\d+")) {
            throw new BadRequestException(message);
        }
        String[] parts = locationCode.split("-");
        try {
            return new int[] {
                Integer.parseInt(parts[0]),
                Integer.parseInt(parts[1]),
                Integer.parseInt(parts[2])
            };
        } catch (NumberFormatException e) {
            throw new BadRequestException(message);
        }
    }
}