package group.australia.warehouse_management_system.feature.item.repository;

import group.australia.warehouse_management_system.feature.item.domain.Item;
import group.australia.warehouse_management_system.feature.item.mapper.ItemMapper;
import group.australia.warehouse_management_system.feature.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ItemRepositoryAdapter implements ItemRepositoryPort {

    private final ItemJpaRepository jpaRepository;
    private final ItemMapper mapper;
    private final LocationRepository locationRepository;

    @Override
    public boolean existsByItemNumberAndLocationId(String itemNumber, Long locationId) {
        return jpaRepository.existsByItemNumberAndLocationId(itemNumber, locationId);
    }

    @Override
    public Optional<Item> findByItemNumberAndLocationId(String itemNumber, Long locationId) {
        return jpaRepository.findByItemNumberAndLocationId(itemNumber, locationId)
                .map(mapper::toDomain);
    }

    @Override
    public Item save(Item item) {
        var entity = mapper.toEntity(item);
        entity.setLocation(locationRepository.getReferenceById(item.getLocation().getId()));
        return mapper.toDomain(jpaRepository.save(entity));
    }
}
