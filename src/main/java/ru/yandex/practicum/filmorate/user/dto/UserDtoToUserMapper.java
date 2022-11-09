package ru.yandex.practicum.filmorate.user.dto;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.core.util.Mapper;
import ru.yandex.practicum.filmorate.user.User;

import java.util.HashSet;

@Component
public class UserDtoToUserMapper implements Mapper<UserDto, User> {
    public User mapFrom(UserDto dto) {
        String name = dto.getName() == null || dto.getName().isBlank() ? dto.getLogin() : dto.getName();

        return User.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .login(dto.getLogin())
                .name(name)
                .birthday(dto.getBirthday())
                .friends(new HashSet<>())
                .build();
    }
}
