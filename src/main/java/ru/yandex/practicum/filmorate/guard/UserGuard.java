package ru.yandex.practicum.filmorate.guard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@Component
@RequiredArgsConstructor
public class UserGuard extends Guard<User> {
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
