package ru.yandex.practicum.filmorate.user.dto;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.core.util.Mapper;
import ru.yandex.practicum.filmorate.user.User;

import java.util.HashSet;

@Component
public class CreateUserDtoToUserMapper implements Mapper<CreateUserDto, User> {
    public User mapFrom(CreateUserDto dto) {
        String name = dto.getName() == null || dto.getName().isBlank() ? dto.getLogin() : dto.getName();

        User.UserBuilder userBuilder = User.builder();
        userBuilder.email(dto.getEmail());
        userBuilder.login(dto.getLogin());
        userBuilder.name(name);
        userBuilder.birthday(dto.getBirthday());
        userBuilder.friends(new HashSet<>());

        return userBuilder.build();
    }
}
