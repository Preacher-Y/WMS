package group.australia.warehouse_management_system.feature.user.service;

import group.australia.warehouse_management_system.feature.user.domain.Role;
import group.australia.warehouse_management_system.feature.user.domain.User;
import group.australia.warehouse_management_system.feature.user.dto.CreateUserDto;
import group.australia.warehouse_management_system.feature.user.dto.UpdateUserDto;
import group.australia.warehouse_management_system.feature.user.exception.UserDuplicateException;
import group.australia.warehouse_management_system.feature.user.exception.UserNotFound;
import group.australia.warehouse_management_system.feature.user.mapper.UserMapper;
import group.australia.warehouse_management_system.feature.user.repository.UserRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepositoryPort userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public User create(CreateUserDto req){

        if (userRepository.existsByEmail(req.email())){
            throw new UserDuplicateException(req.email());
        }

        var user = userMapper.toDomainFromCreate(req);
        var prepared = user.toBuilder()
                .password(passwordEncoder.encode(user.getPassword()))
                .role(Role.USER)
                .build();

        try {

            return userRepository.save(prepared);

        } catch (DataIntegrityViolationException ex) {
            throw new UserDuplicateException(prepared.getEmail());
        }
    }

    public User update(Long id, UpdateUserDto update){

        var user = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFound(id));

        user.rename(update.name());

        if (update.active()) {
            user.activate();
        } else {
            user.deactivate();
        }

        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new UserDuplicateException(user.getEmail());
        }
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public User getById(Long id){
        return userRepository.findById(id)
                .orElseThrow(()-> new UserNotFound(id));
    }
}
