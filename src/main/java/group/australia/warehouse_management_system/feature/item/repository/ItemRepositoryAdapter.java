package group.australia.warehouse_management_system.feature.item.repository;

import group.australia.warehouse_management_system.feature.item.domain.Item;
import group.australia.warehouse_management_system.feature.item.mapper.ItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemRepositoryAdapter implements ItemRepositoryPort {

    private final ItemJpaRepository jpaRepository;
    private final ItemMapper mapper;

    @Override
    public boolean existsByItemNumberAndLocationId(String itemNumber, Long locationId) {
        return jpaRepository.existsByItemNumberAndLocationId(itemNumber, locationId);
    }

    @Override
    public Item save(Item item) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(item)));
    }
}
