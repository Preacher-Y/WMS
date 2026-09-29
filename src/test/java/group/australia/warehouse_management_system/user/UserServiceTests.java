package group.australia.warehouse_management_system.user;

import group.australia.warehouse_management_system.feature.user.domain.Role;
import group.australia.warehouse_management_system.feature.user.domain.User;
import group.australia.warehouse_management_system.feature.user.dto.CreateUserDto;
import group.australia.warehouse_management_system.feature.user.dto.UpdateUserDto;
import group.australia.warehouse_management_system.feature.user.exception.UserDuplicateException;
import group.australia.warehouse_management_system.feature.user.exception.UserNotFound;
import group.australia.warehouse_management_system.feature.user.mapper.UserMapper;
import group.australia.warehouse_management_system.feature.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    private InMemoryUserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        userService = new UserService(userRepository, userMapper, passwordEncoder);
    }

    @Test
    void Should_create_user_if_email_does_not_exist_yet() {
        var dto = new CreateUserDto("preacher@gmail.com", "Preacher Y", "secret");

        var userDomain = User.builder()
                .email("preacher@gmail.com")
                .name("Preacher Y")
                .password("secret")
                .build();

        when(userMapper.toDomainFromCreate(Mockito.any(CreateUserDto.class))).thenReturn(userDomain);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed-secret");

        var result = userService.create(dto);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getEmail()).isEqualTo("preacher@gmail.com");
        Assertions.assertThat(result.getName()).isEqualTo("Preacher Y");
        Assertions.assertThat(result.isActive()).isTrue();
        Assertions.assertThat(result.getRole()).isEqualTo(Role.USER);
        Assertions.assertThat(result.getId()).isGreaterThan(0);
    }

    @Test
    void Should_throw_when_email_already_exists() {
        userRepository.save(User.builder()
                .email("preacher@gmail.com")
                .name("Preacher Y")
                .password("hashed-secret")
                .role(Role.USER)
                .build());

        var dto = new CreateUserDto("preacher@gmail.com", "Preacher Y", "secret");

        Assertions.assertThatThrownBy(() -> userService.create(dto))
                .isInstanceOf(UserDuplicateException.class);
    }

    @Test
    void Should_update_user_when_it_exists() {
        var saved = userRepository.save(User.builder()
                .email("preacher@gmail.com")
                .name("Preacher Y")
                .password("hashed-secret")
                .active(true)
                .role(Role.USER)
                .version(0)
                .build());

        var update = new UpdateUserDto("Preacher Y Updated", false);

        var result = userService.update(saved.getId(), update);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(saved.getId());
        Assertions.assertThat(result.getName()).isEqualTo("Preacher Y Updated");
        Assertions.assertThat(result.getEmail()).isEqualTo("preacher@gmail.com");
        Assertions.assertThat(result.isActive()).isFalse();
    }

    @Test
    void Should_activate_user_on_update() {
        var saved = userRepository.save(User.builder()
                .email("preacher@gmail.com")
                .name("Preacher Y")
                .password("hashed-secret")
                .active(false)
                .role(Role.USER)
                .version(0)
                .build());

        var update = new UpdateUserDto("Preacher Y", true);

        var result = userService.update(saved.getId(), update);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.isActive()).isTrue();
    }

    @Test
    void Should_throw_when_updating_non_existent_user() {
        var update = new UpdateUserDto("Any Name", true);

        Assertions.assertThatThrownBy(() -> userService.update(999L, update))
                .isInstanceOf(UserNotFound.class);
    }

    @Test
    void Should_return_all_users() {
        userRepository.save(User.builder().email("preacher@gmail.com").name("Preacher Y").password("pw").role(Role.USER).build());
        userRepository.save(User.builder().email("second@gmail.com").name("Second User").password("pw").role(Role.USER).build());

        var result = userService.getAll();

        Assertions.assertThat(result).hasSize(2);
    }

    @Test
    void Should_return_user_by_id() {
        var saved = userRepository.save(User.builder()
                .email("preacher@gmail.com")
                .name("Preacher Y")
                .password("hashed-secret")
                .role(Role.USER)
                .build());

        var result = userService.getById(saved.getId());

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(saved.getId());
        Assertions.assertThat(result.getEmail()).isEqualTo("preacher@gmail.com");
    }

    @Test
    void Should_throw_when_getting_non_existent_user_by_id() {
        Assertions.assertThatThrownBy(() -> userService.getById(999L))
                .isInstanceOf(UserNotFound.class);
    }
}
