package group.australia.warehouse_management_system.feature.location.service;

import org.springframework.stereotype.Service;

import group.australia.warehouse_management_system.common.exceptions.NotFoundException;
import group.australia.warehouse_management_system.feature.location.dto.LocationRequestDto;
import group.australia.warehouse_management_system.feature.location.dto.LocationResponseDto;
import group.australia.warehouse_management_system.feature.location.mapper.LocationMapper;
import group.australia.warehouse_management_system.feature.location.repository.LocationRepository;
import group.australia.warehouse_management_system.feature.warehouse.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import group.australia.warehouse_management_system.common.exceptions.ConflictException;

@Service 
@RequiredArgsConstructor 
public class LocationService {
    private final LocationMapper locationmapper;
    private final LocationRepository locationRepository;
    private final WarehouseRepository warehouseRepository;
    
    public LocationResponseDto insertNewLocation(Long warehouseId,
        LocationRequestDto request){

            var warehouse= warehouseRepository.findById(warehouseId)
            .orElseThrow(() -> new NotFoundException("Warehouse not found"));

            var location=locationmapper.toEntityFromRequest(request);
           var locationCode=request.row() + "-"
           + request.section() + "-"
           + request.shelf();
    if(locationRepository.existsByWarehouseIdAndLocationCode(warehouseId, locationCode)){
        throw new ConflictException("Location already exists");
    }
    location.setLocationCode(locationCode);
    location.setWarehouse(warehouse);
    var savedLocation = locationRepository.save(location);
     return locationmapper.toResponseDto(savedLocation);
        }

}
