package ru.yandex.practicum.filmorate.user.dto;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.core.util.Mapper;
import ru.yandex.practicum.filmorate.user.User;

@Component
public class UpdateUserDtoToUserMapper implements Mapper<UpdateUserDto, User> {
    public User mapFrom(UpdateUserDto dto) {
        String name = dto.getName() == null || dto.getName().isBlank() ? dto.getLogin() : dto.getName();

        User.UserBuilder userBuilder = User.builder();
        userBuilder.id(dto.getId());
        userBuilder.email(dto.getEmail());
        userBuilder.login(dto.getLogin());
        userBuilder.name(name);
        userBuilder.birthday(dto.getBirthday());

        return userBuilder.build();
    }
}
