package ru.yandex.practicum.filmorate.guard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class UserGuard extends Guard<User> {
    private final UserRepository userRepository;

    @Override
    protected String getGuardClass() {
        return "User";
    }

    @Override
    protected User checkMethod(int id) {
        return userRepository.getById(id);
    }
}
