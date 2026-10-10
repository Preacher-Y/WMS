package group.australia.warehouse_management_system.feature.user.repository;

import group.australia.warehouse_management_system.feature.user.domain.User;
import group.australia.warehouse_management_system.feature.user.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
@AllArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id).map(userMapper::toDomain);
    }

    @Override
    public User save(User user) {
        return userMapper.toDomain(userJpaRepository.save(userMapper.toEntity(user)));
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll().stream().map(userMapper::toDomain).toList();
    }
}
