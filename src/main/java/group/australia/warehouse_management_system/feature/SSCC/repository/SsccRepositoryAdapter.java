package group.australia.warehouse_management_system.feature.SSCC.repository;

import group.australia.warehouse_management_system.feature.SSCC.domain.Sscc;
import group.australia.warehouse_management_system.feature.SSCC.mapper.SsccMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SsccRepositoryAdapter implements SsccRepositoryPort {
    private final SsccJpaRepository jpaRepository;
    private final SsccMapper mapper;

    public boolean existsBySscc(String sscc) {
        return jpaRepository.existsBySscc(sscc);
    }

    public Sscc save(Sscc sscc) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(sscc)));
    }
}
