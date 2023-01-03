package ru.yandex.practicum.filmorate.storage.user;

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
public class DbUserStorageTest {
    private final DbUserStorage userStorage;

    @Test
    void getAll_shouldReturnEmptyList() {
        assertThat(userStorage.getAll()).isEmpty();
    }

    @Test
    void getAll_shouldReturnListOfTwoUsers() {
        userStorage.create(getUser(1, "test1"));
        userStorage.create(getUser(2, "test2"));

        assertThat(userStorage.getAll()).hasSize(2);
    }

    @Test
    void getById_shouldReturnNull_IfUserIsNotExists() {
        assertThat(userStorage.getById(1)).isNull();
    }

    @Test
    void getById_shouldReturnUser() {
        User user = userStorage.create(getUser(1, "test"));

        assertThat(userStorage.getById(user.getId())).isEqualTo(getUser(user.getId(), "test"));
    }

    @Test
    void getByEmail_shouldReturnNull_IfUserIsNotExists() {
        assertThat(userStorage.getByEmail("test")).isNull();
    }

    @Test
    void getByEmail_shouldReturnUser() {
        User user = userStorage.create(getUser(1, "test"));

        assertThat(userStorage.getByEmail(user.getEmail())).isEqualTo(getUser(user.getId(), "test"));
    }

    @Test
    void update_shouldReturnNull_IfUserIsNotExists() {
        assertThat(userStorage.update(getUser(1, "test"))).isNull();
    }

    @Test
    void update_shouldUpdateUser() {
        User user = userStorage.create(getUser(0, "test"));

        assertThat(userStorage.update(user.withName("new name")))
                .hasFieldOrPropertyWithValue("name", "new name");
    }

    @Test
    void delete_shouldReturnNull_IfUserIsNotExists() {
        assertThat(userStorage.update(getUser(1, "test"))).isNull();
    }

    @Test
    void delete_shouldDeleteUser() {
        User user = userStorage.create(getUser(0, "test"));

        assertThat(userStorage.getAll()).hasSize(1);
        userStorage.delete(user.getId());
        assertThat(userStorage.getAll()).isEmpty();
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
