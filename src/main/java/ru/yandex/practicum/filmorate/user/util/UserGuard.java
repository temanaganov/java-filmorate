package ru.yandex.practicum.filmorate.user.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.core.util.Guard;
import ru.yandex.practicum.filmorate.user.model.User;
import ru.yandex.practicum.filmorate.user.storage.UserStorage;

@Component
@RequiredArgsConstructor
public class UserGuard extends Guard<User> {
    @Qualifier("dbUserStorage")
    private final UserStorage userStorage;

    @Override
    protected String getGuardClass() {
        return "User";
    }

    @Override
    protected User checkMethod(int id) {
        return userStorage.getById(id);
    }
}
