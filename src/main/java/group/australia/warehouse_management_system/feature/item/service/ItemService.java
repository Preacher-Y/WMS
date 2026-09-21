package group.australia.warehouse_management_system.feature.item.service;

import group.australia.warehouse_management_system.common.exceptions.ConflictException;
import group.australia.warehouse_management_system.common.exceptions.NotFoundException;
import group.australia.warehouse_management_system.feature.item.domain.Item;
import group.australia.warehouse_management_system.feature.item.dto.ItemRequestDto;
import group.australia.warehouse_management_system.feature.item.dto.ResponseItemDto;
import group.australia.warehouse_management_system.feature.item.entity.ItemEntity;
import group.australia.warehouse_management_system.feature.item.mapper.ItemMapper;
import group.australia.warehouse_management_system.feature.item.repository.ItemRepository;
import group.australia.warehouse_management_system.feature.cartonHeader.repository.CartonHeaderRepositoryPort;
import group.australia.warehouse_management_system.feature.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;
    private final LocationRepository locationRepository;
    private final CartonHeaderRepositoryPort cartonHeaderRepository;

    public ResponseItemDto insertNewItem(ItemRequestDto req) {
        if (itemRepository.existsByItemNumberAndLocationId(req.itemNumber(), req.locationId()))
            throw new ConflictException("Item already exists at this location");

        var location = locationRepository.findById(req.locationId())
                .orElseThrow(() -> new NotFoundException("Location not found"));
        var cartonHeader = cartonHeaderRepository.findById(req.cartonHeaderId())
                .orElseThrow(() -> new NotFoundException("Carton Header not found"));

        var item = itemMapper.toDomainFromCreate(req).toBuilder()
                .location(location).cartonHeader(cartonHeader).build();
        var entity = itemMapper.toEntity(item);
        try {
            var saved = itemRepository.save(entity);
            return itemMapper.toDto(itemMapper.toDomain(saved));
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException("Item conflicts with existing data");
        }
    }
}
