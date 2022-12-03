package ru.yandex.practicum.filmorate.user.storage;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.core.storage.InMemoryStorageTest;
import ru.yandex.practicum.filmorate.user.model.User;

import java.time.LocalDate;
import java.util.Collections;

public class InMemoryUserStorageTest extends InMemoryStorageTest<User> {
    @BeforeEach
    void beforeEach() {
        storage = new InMemoryUserStorage();
    }

    @Override
    protected User getEntity(int id) {
        return new User(
                id,
                "test@test.test",
                "Test login",
                "Test name",
                LocalDate.EPOCH,
                Collections.emptySet()
        );
    }

    @Override
    protected User getEntityForUpdate(int id) {
        return new User(
                id,
                "test@test.test",
                "Test login updated",
                "Test name updated",
                LocalDate.EPOCH,
                Collections.emptySet()
        );
    }
}
