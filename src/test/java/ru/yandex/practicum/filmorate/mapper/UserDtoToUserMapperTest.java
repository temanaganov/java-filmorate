package ru.yandex.practicum.filmorate.mapper;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

public class UserDtoToUserMapperTest {
    private final UserDtoToUserMapper userDtoToUserMapper = new UserDtoToUserMapper();

    @Test
    void mapFrom_shouldCreateUserWithSameFields() {
        UserDto userDto = UserDto.builder()
                .id(1)
                .email("test@test.test")
                .login("test_login")
                .name("Test name")
                .birthday(LocalDate.EPOCH)
                .build();

        User user = userDtoToUserMapper.mapFrom(userDto);

        assertAll(() -> {
            assertThat(user.getId()).isEqualTo(userDto.getId());
            assertThat(user.getEmail()).isEqualTo(userDto.getEmail());
            assertThat(user.getLogin()).isEqualTo(userDto.getLogin());
            assertThat(user.getName()).isEqualTo(userDto.getName());
            assertThat(user.getBirthday()).isEqualTo(userDto.getBirthday());
        });
    }

    @Test
    void mapFrom_shouldCreateUserWithNameEqualsLogin_ifNameIsNotGiven() {
        UserDto userDto = UserDto.builder()
                .id(1)
                .email("test@test.test")
                .login("test_login")
                .name(null)
                .birthday(LocalDate.EPOCH)
                .build();

        User user = userDtoToUserMapper.mapFrom(userDto);

        assertAll(() -> {
            assertThat(user.getId()).isEqualTo(userDto.getId());
            assertThat(user.getEmail()).isEqualTo(userDto.getEmail());
            assertThat(user.getLogin()).isEqualTo(userDto.getLogin());
            assertThat(user.getName()).isEqualTo(userDto.getLogin());
            assertThat(user.getBirthday()).isEqualTo(userDto.getBirthday());
        });
    }
}
