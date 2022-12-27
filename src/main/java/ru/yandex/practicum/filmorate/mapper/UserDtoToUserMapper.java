package ru.yandex.practicum.filmorate.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.user.UserDto;
import ru.yandex.practicum.filmorate.model.user.User;

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
