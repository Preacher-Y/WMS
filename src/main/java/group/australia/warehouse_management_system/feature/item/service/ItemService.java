package group.australia.warehouse_management_system.feature.item.service;

import group.australia.warehouse_management_system.common.exceptions.NotFoundException;
import group.australia.warehouse_management_system.feature.item.domain.Item;
import group.australia.warehouse_management_system.feature.item.repository.ItemRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepositoryPort itemRepository;

    public Item getByItemNumberAndLocationId(String itemNumber, Long locationId) {
        return itemRepository.findByItemNumberAndLocationId(itemNumber, locationId)
                .orElseThrow(() -> new NotFoundException("Item " + itemNumber + " not found"));
    }

    public Optional<Item> findByItemNumberAndLocationId(String itemNumber, Long locationId) {
        return itemRepository.findByItemNumberAndLocationId(itemNumber, locationId);
    }

    public Item save(Item item) {
        return itemRepository.save(item);
    }
}
