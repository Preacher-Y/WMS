package group.australia.warehouse_management_system.feature.item.service;

import group.australia.warehouse_management_system.common.exceptions.BadRequestException;
import group.australia.warehouse_management_system.common.exceptions.ConflictException;
import group.australia.warehouse_management_system.common.exceptions.NotFoundException;
import group.australia.warehouse_management_system.feature.cartonHeader.repository.CartonHeaderRepository;
import group.australia.warehouse_management_system.feature.item.dto.ItemRequestDto;
import group.australia.warehouse_management_system.feature.item.dto.ResponseItemDto;
import group.australia.warehouse_management_system.feature.item.entity.Item;
import group.australia.warehouse_management_system.feature.item.mapper.ItemMapper;
import group.australia.warehouse_management_system.feature.item.repository.ItemRepository;
import group.australia.warehouse_management_system.feature.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final LocationRepository locationRepository;
    private final CartonHeaderRepository cartonHeaderRepository;
    private final ItemMapper itemMapper;

    public ResponseItemDto createItem(ItemRequestDto request) {
        var location = locationRepository.findById(request.locationId())
                .orElseThrow(() -> new NotFoundException("Location not found"));

        var cartonHeader = cartonHeaderRepository.findById(request.cartonHeaderId())
                .orElseThrow(() -> new NotFoundException("Carton header not found"));

        if (itemRepository.existsByItemNumberAndLocationId(request.itemNumber(), request.locationId())) {
            throw new ConflictException(
                    "Item with number '" + request.itemNumber() + "' already exists at this location"
            );
        }

        Item item = Item.builder()
                .itemNumber(request.itemNumber())
                .quantity(request.quantity())
                .location(location)
                .cartonHeader(cartonHeader)
                .build();

        return itemMapper.toDto(itemRepository.save(item));
    }

    public ResponseItemDto updateQuantity(Long itemId, Integer newQuantity) {
        if (newQuantity < 0) {
            throw new BadRequestException("Quantity cannot be negative");
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));

        item.setQuantity(newQuantity);
        return itemMapper.toDto(itemRepository.save(item));
    }
}
