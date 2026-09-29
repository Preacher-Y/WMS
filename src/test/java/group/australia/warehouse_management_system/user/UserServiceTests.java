package group.australia.warehouse_management_system.user;

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

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    private InMemoryUserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        userService = new UserService(userRepository, userMapper);
    }

    @Test
    void Should_create_user_if_email_does_not_exist_yet() {
        var dto = new CreateUserDto("john@example.com", "John Doe", "secret");

        var userDomain = User.builder()
                .email("john@example.com")
                .name("John Doe")
                .password("secret")
                .build();

        when(userMapper.toDomainFromCreate(Mockito.any(CreateUserDto.class))).thenReturn(userDomain);

        var result = userService.create(dto);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getEmail()).isEqualTo("john@example.com");
        Assertions.assertThat(result.getName()).isEqualTo("John Doe");
        Assertions.assertThat(result.isActive()).isTrue();
        Assertions.assertThat(result.getId()).isGreaterThan(0);
    }

    @Test
    void Should_throw_when_email_already_exists() {
        userRepository.save(User.builder()
                .email("john@example.com")
                .name("John Doe")
                .password("secret")
                .build());

        var dto = new CreateUserDto("john@example.com", "John Doe", "secret");

        Assertions.assertThatThrownBy(() -> userService.create(dto))
                .isInstanceOf(UserDuplicateException.class);
    }

    @Test
    void Should_update_user_when_it_exists() {
        userRepository.save(User.builder()
                .email("john@example.com")
                .name("John Doe")
                .password("secret")
                .active(true)
                .version(0)
                .build());

        var update = new UpdateUserDto("John Updated", false);

        var result = userService.update("john@example.com", update);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isGreaterThan(0);
        Assertions.assertThat(result.getName()).isEqualTo("John Updated");
        Assertions.assertThat(result.getEmail()).isEqualTo("john@example.com");
        Assertions.assertThat(result.isActive()).isFalse();
    }

    @Test
    void Should_activate_user_on_update() {
        userRepository.save(User.builder()
                .email("john@example.com")
                .name("John Doe")
                .password("secret")
                .active(false)
                .version(0)
                .build());

        var update = new UpdateUserDto("John Doe", true);

        var result = userService.update("john@example.com", update);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.isActive()).isTrue();
    }

    @Test
    void Should_throw_when_updating_non_existent_user() {
        var update = new UpdateUserDto("Any Name", true);

        Assertions.assertThatThrownBy(() -> userService.update("nobody@example.com", update))
                .isInstanceOf(UserNotFound.class);
    }

    @Test
    void Should_return_all_users() {
        userRepository.save(User.builder().email("a@example.com").name("Alice").password("pw").build());
        userRepository.save(User.builder().email("b@example.com").name("Bob").password("pw").build());

        var result = userService.getAll();

        Assertions.assertThat(result).hasSize(2);
    }

    @Test
    void Should_return_user_by_email() {
        userRepository.save(User.builder()
                .email("john@example.com")
                .name("John Doe")
                .password("secret")
                .build());

        var result = userService.getByEmail("john@example.com");

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void Should_throw_when_getting_non_existent_user_by_email() {
        Assertions.assertThatThrownBy(() -> userService.getByEmail("nobody@example.com"))
                .isInstanceOf(UserNotFound.class);
    }
}