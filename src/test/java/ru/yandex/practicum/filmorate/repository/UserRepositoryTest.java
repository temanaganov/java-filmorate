package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Sql(statements = "DELETE FROM USERS")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserRepositoryTest {
    private final UserRepository userRepository;

    @Test
    void getAll_shouldReturnEmptyList() {
        assertThat(userRepository.getAll()).isEmpty();
    }

    @Test
    void getAll_shouldReturnListOfTwoUsers() {
        userRepository.create(getUser(1, "test1"));
        userRepository.create(getUser(2, "test2"));

        assertThat(userRepository.getAll()).hasSize(2);
    }

    @Test
    void getById_shouldReturnNull_IfUserIsNotExists() {
        assertThat(userRepository.getById(1)).isNull();
    }

    @Test
    void getById_shouldReturnUser() {
        User user = userRepository.create(getUser(1, "test"));

        assertThat(userRepository.getById(user.getId())).isEqualTo(getUser(user.getId(), "test"));
    }

    @Test
    void getByEmail_shouldReturnNull_IfUserIsNotExists() {
        assertThat(userRepository.getByEmail("test")).isNull();
    }

    @Test
    void getByEmail_shouldReturnUser() {
        User user = userRepository.create(getUser(1, "test"));

        assertThat(userRepository.getByEmail(user.getEmail())).isEqualTo(getUser(user.getId(), "test"));
    }

    @Test
    void update_shouldReturnNull_IfUserIsNotExists() {
        assertThat(userRepository.update(getUser(1, "test"))).isNull();
    }

    @Test
    void update_shouldUpdateUser() {
        User user = userRepository.create(getUser(0, "test"));

        assertThat(userRepository.update(user.withName("new name")))
                .hasFieldOrPropertyWithValue("name", "new name");
    }

    @Test
    void delete_shouldReturnNull_IfUserIsNotExists() {
        assertThat(userRepository.update(getUser(1, "test"))).isNull();
    }

    @Test
    void delete_shouldDeleteUser() {
        User user = userRepository.create(getUser(0, "test"));

        assertThat(userRepository.getAll()).hasSize(1);
        userRepository.delete(user.getId());
        assertThat(userRepository.getAll()).isEmpty();
    }

    private User getUser(int id, String email) {
        return User.builder()
                .id(id)
                .email(email)
                .login("Test login")
                .name("Test name")
                .birthday(LocalDate.EPOCH)
                .build();
    }
}
