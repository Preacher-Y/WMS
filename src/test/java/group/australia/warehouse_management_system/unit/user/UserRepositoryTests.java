package group.australia.warehouse_management_system.unit.user;

import group.australia.warehouse_management_system.feature.user.domain.Role;
import group.australia.warehouse_management_system.feature.user.entity.UserEntity;
import group.australia.warehouse_management_system.feature.user.repository.UserJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserRepositoryTests {

    @Autowired
    private UserJpaRepository userRepository;
    private UserEntity savedUser;

    @BeforeEach
    void setUp() {
        savedUser = userRepository.save(UserEntity.builder()
                .email("preacher@gmail.com")
                .name("Preacher Y")
                .password("hashed-secret")
                .active(true)
                .role(Role.USER)
                .build());
    }

    @Test
    void Should_save_and_return_user() {
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
        Assertions.assertThat(savedUser.getEmail()).isEqualTo("preacher@gmail.com");
        Assertions.assertThat(savedUser.getName()).isEqualTo("Preacher Y");
    }

    @Test
    void Should_find_user_by_id() {
        var result = userRepository.findById(savedUser.getId());

        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().getId()).isEqualTo(savedUser.getId());
        Assertions.assertThat(result.get().getEmail()).isEqualTo("preacher@gmail.com");
    }

    @Test
    void Should_return_true_when_email_exists() {
        var res = userRepository.existsByEmail(savedUser.getEmail());

        Assertions.assertThat(res).isTrue();
    }

    @Test
    void Should_return_false_when_email_does_not_exist() {
        var res = userRepository.existsByEmail("nobody@example.com");

        Assertions.assertThat(res).isFalse();
    }

    @Test
    void Should_throw_on_duplicate_email() {
        var duplicate = UserEntity.builder()
                .email("preacher@gmail.com")
                .name("Another Preacher")
                .password("pass")
                .active(true)
                .role(Role.USER)
                .build();

        Assertions.assertThatThrownBy(() -> userRepository.saveAndFlush(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void Should_throw_on_optimistic_locking_conflict() {
        var originalVersion = savedUser.getVersion();

        savedUser.setName("First Update");
        userRepository.saveAndFlush(savedUser);

        var stale = UserEntity.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .name("Stale Update")
                .password(savedUser.getPassword())
                .active(savedUser.getActive())
                .role(savedUser.getRole())
                .version(originalVersion)
                .build();

        Assertions.assertThatThrownBy(() -> userRepository.saveAndFlush(stale))
                .isInstanceOf(ObjectOptimisticLockingFailureException.class);
    }
}
