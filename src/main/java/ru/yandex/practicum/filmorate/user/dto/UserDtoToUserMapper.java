package ru.yandex.practicum.filmorate.user.dto;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.core.util.Mapper;
import ru.yandex.practicum.filmorate.user.model.User;

@Component
public class UserDtoToUserMapper implements Mapper<UserDto, User> {
    public User mapFrom(UserDto dto) {
        String name = dto.getName() == null || dto.getName().isBlank() ? dto.getLogin() : dto.getName();

        return new User(
                dto.getId(),
                dto.getEmail(),
                dto.getLogin(),
                name,
                dto.getBirthday()
        );
    }
}
