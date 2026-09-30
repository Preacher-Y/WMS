package group.australia.warehouse_management_system.feature.SSCC.repository;

import group.australia.warehouse_management_system.feature.SSCC.domain.Sscc;
import group.australia.warehouse_management_system.feature.SSCC.mapper.SsccMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SsccRepositoryAdapter implements SsccRepositoryPort {
    private final SsccJpaRepository jpaRepository;
    private final SsccMapper mapper;

    @Override
    public boolean existsBySscc(String sscc) {
        return jpaRepository.existsBySscc(sscc);
    }

    @Override
    public Optional<Sscc> findBySscc(String sscc) {
        return jpaRepository.findBySscc(sscc).map(mapper::toDomain);
    }

    @Override
    public Sscc save(Sscc sscc) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(sscc)));
    }
}
