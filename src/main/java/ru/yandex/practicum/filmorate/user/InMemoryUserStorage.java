package ru.yandex.practicum.filmorate.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.core.storage.InMemoryStorage;

@Component
public class InMemoryUserStorage extends InMemoryStorage<User> implements UserStorage {
    @Override
    protected User withId(User user, int id) {
        return user.withId(id);
    }
}
