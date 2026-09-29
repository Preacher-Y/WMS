package group.australia.warehouse_management_system.feature.user.service;

import group.australia.warehouse_management_system.feature.user.domain.User;
import group.australia.warehouse_management_system.feature.user.dto.CreateUserDto;
import group.australia.warehouse_management_system.feature.user.dto.UpdateUserDto;
import group.australia.warehouse_management_system.feature.user.exception.UserDuplicateException;
import group.australia.warehouse_management_system.feature.user.exception.UserNotFound;
import group.australia.warehouse_management_system.feature.user.mapper.UserMapper;
import group.australia.warehouse_management_system.feature.user.repository.UserRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepositoryPort userRepository;
    private final UserMapper userMapper;

    public User create(CreateUserDto req){

        if (userRepository.existsByEmail(req.email())){
            throw new UserDuplicateException(req.email());
        }

        var user = userMapper.toDomainFromCreate(req);

        try {

            return userRepository.save(user);

        } catch (DataIntegrityViolationException ex) {
            throw new UserDuplicateException(user.getEmail());
        }
    }

    public User update(String email,UpdateUserDto update){

        var user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UserNotFound(email));

        user.rename(update.name());

        if (update.active()) {
            user.activate();
        } else {
            user.deactivate();
        }

        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new UserDuplicateException(email);
        }
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public User getByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new UserNotFound(email));
    }
}
