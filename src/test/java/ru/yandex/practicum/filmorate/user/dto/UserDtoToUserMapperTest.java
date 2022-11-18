package ru.yandex.practicum.filmorate.user.dto;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.user.User;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

public class UserDtoToUserMapperTest {
    private final UserDtoToUserMapper userDtoToUserMapper = new UserDtoToUserMapper();

    @Test
    void mapFrom_shouldCreateUserWithSameFields() {
        UserDto userDto = new UserDto(
                1,
                "test@test.test",
                "test_login",
                "Test name",
                LocalDate.EPOCH
        );

        User user = userDtoToUserMapper.mapFrom(userDto);

        assertAll(() -> {
            assertThat(user.getId()).isEqualTo(userDto.getId());
            assertThat(user.getEmail()).isEqualTo(userDto.getEmail());
            assertThat(user.getLogin()).isEqualTo(userDto.getLogin());
            assertThat(user.getName()).isEqualTo(userDto.getName());
            assertThat(user.getBirthday()).isEqualTo(userDto.getBirthday());
            assertThat(user.getFriends()).isEqualTo(Collections.emptySet());
        });
    }

    @Test
    void mapFrom_shouldCreateUserWithNameEqualsLogin_ifNameIsNotGiven() {
        UserDto userDto = new UserDto(
                1,
                "test@test.test",
                "test_login",
                null,
                LocalDate.EPOCH
        );

        User user = userDtoToUserMapper.mapFrom(userDto);

        assertAll(() -> {
            assertThat(user.getId()).isEqualTo(userDto.getId());
            assertThat(user.getEmail()).isEqualTo(userDto.getEmail());
            assertThat(user.getLogin()).isEqualTo(userDto.getLogin());
            assertThat(user.getName()).isEqualTo(userDto.getLogin());
            assertThat(user.getBirthday()).isEqualTo(userDto.getBirthday());
            assertThat(user.getFriends()).isEqualTo(Collections.emptySet());
        });
    }
}
