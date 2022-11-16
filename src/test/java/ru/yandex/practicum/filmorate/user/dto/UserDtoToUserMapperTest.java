package ru.yandex.practicum.filmorate.user.dto;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.user.User;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
            assertEquals(userDto.getId(), user.getId());
            assertEquals(userDto.getEmail(), user.getEmail());
            assertEquals(userDto.getLogin(), user.getLogin());
            assertEquals(userDto.getName(), user.getName());
            assertEquals(userDto.getBirthday(), user.getBirthday());
            assertEquals(Collections.emptySet(), user.getFriends());
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
            assertEquals(userDto.getId(), user.getId());
            assertEquals(userDto.getEmail(), user.getEmail());
            assertEquals(userDto.getLogin(), user.getLogin());
            assertEquals(userDto.getLogin(), user.getName());
            assertEquals(userDto.getBirthday(), user.getBirthday());
            assertEquals(Collections.emptySet(), user.getFriends());
        });
    }
}
