package group.australia.warehouse_management_system.feature.location.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import group.australia.warehouse_management_system.common.exceptions.ConflictException;
import group.australia.warehouse_management_system.common.exceptions.NotFoundException;
import group.australia.warehouse_management_system.feature.location.domain.Location;
import group.australia.warehouse_management_system.feature.location.repository.LocationRepositoryAdapter;
import group.australia.warehouse_management_system.feature.warehouse.repository.WarehouseJpaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepositoryAdapter locationRepositoryAdapter;
    private final WarehouseJpaRepository warehouseRepository;

    @Transactional
    public Location insertNewLocation(Long warehouseId, Location location) {

        if (!warehouseRepository.existsById(warehouseId)) {
            throw new NotFoundException("Warehouse not found with id: " + warehouseId);
        }

        if (locationRepositoryAdapter.exists(
                warehouseId,
                location.getRow(),
                location.getSection(),
                location.getShelf())) {
            throw new ConflictException("Location already exists in warehouse with id: " + warehouseId);
        }

        location.setWarehouseId(warehouseId);
        return locationRepositoryAdapter.save(location);
    }
}