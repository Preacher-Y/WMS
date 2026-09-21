package group.australia.warehouse_management_system.feature.item.service;

import group.australia.warehouse_management_system.feature.item.exception.ItemCartonHeaderNotFoundException;
import group.australia.warehouse_management_system.feature.item.exception.ItemDuplicateException;
import group.australia.warehouse_management_system.feature.item.exception.ItemLocationNotFoundException;
import group.australia.warehouse_management_system.feature.item.domain.Item;
import group.australia.warehouse_management_system.feature.item.dto.ItemRequestDto;
import group.australia.warehouse_management_system.feature.item.mapper.ItemMapper;
import group.australia.warehouse_management_system.feature.item.repository.ItemRepositoryPort;
import group.australia.warehouse_management_system.feature.cartonHeader.repository.CartonHeaderRepositoryPort;
import group.australia.warehouse_management_system.feature.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemMapper itemMapper;
    private final ItemRepositoryPort itemRepository;
    private final LocationRepository locationRepository;
    private final CartonHeaderRepositoryPort cartonHeaderRepository;

    public Item insertNewItem(ItemRequestDto req) {
        if (itemRepository.existsByItemNumberAndLocationId(req.itemNumber(), req.locationId()))
            throw new ItemDuplicateException(req.itemNumber(), req.locationId());

        var location = locationRepository.findById(req.locationId())
                .orElseThrow(() -> new ItemLocationNotFoundException(req.locationId()));
        var cartonHeader = cartonHeaderRepository.findById(req.cartonHeaderId())
                .orElseThrow(() -> new ItemCartonHeaderNotFoundException(req.cartonHeaderId()));

        var item = itemMapper.toDomainFromCreate(req)
                .withLocation(location)
                .withCartonHeader(cartonHeader);
        try {
            return itemRepository.save(item);
        } catch (DataIntegrityViolationException ex) {
            throw new ItemDuplicateException(req.itemNumber(), req.locationId());
        }
    }
}
