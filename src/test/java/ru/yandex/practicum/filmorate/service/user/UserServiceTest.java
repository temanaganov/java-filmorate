package ru.yandex.practicum.filmorate.service.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.user.UserServiceImpl;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.model.user.UserDto;
import ru.yandex.practicum.filmorate.mapper.UserDtoToUserMapper;
import ru.yandex.practicum.filmorate.guard.UserGuard;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserStorage userStorage;

    @Mock
    private UserGuard userGuard;

    @Mock
    private UserDtoToUserMapper userDtoToUserMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getAll_shouldReturnEmptyList_ifStorageIsEmpty() {
        when(userStorage.getAll()).thenReturn(Collections.emptyList());
        assertThat(userService.getAll()).isEqualTo(Collections.emptyList());
    }

    @Test()
    void getAll_shouldReturnThreeUsers_ifStorageHasThreeUsers() {
        List<User> users = List.of(getUser(1), getUser(2), getUser(3));

        when(userStorage.getAll()).thenReturn(users);

        assertThat(userService.getAll()).isEqualTo(users);
    }

    @Test()
    void getById_shouldReturnUser_ifStorageHasIt() {
        int id = 1;
        User user = getUser(id);

        when(userGuard.checkIfExists(id)).thenReturn(user);

        User resultUser = userService.getById(id);

        assertThat(resultUser).isEqualTo(user);
        verify(userGuard).checkIfExists(id);
    }

    @Test()
    void getById_shouldThrowNotFoundException_ifStorageHasNoUser() {
        int id = 1;
        when(userGuard.checkIfExists(id)).thenThrow(new NotFoundException("User", id));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.getById(id));
        assertThat(exception.getMessage()).isEqualTo("User with id=" + id + " not found");
    }

    @Test()
    void create_shouldReturnNewUser() {
        UserDto dto = getUserDto(0);
        User user = getUser(0);
        User resultUser = getUser(1);

        when(userDtoToUserMapper.mapFrom(dto)).thenReturn(user);
        when(userStorage.create(user)).thenReturn(resultUser);

        assertThat(userService.create(dto)).isEqualTo(resultUser);
        verify(userStorage).create(user);
    }

    @Test()
    void update_shouldReturnUpdatedUser() {
        int id = 1;
        String newName = "New name";
        UserDto dto = getUserDto(id).withName(newName);
        User oldUser = getUser(id);
        User newUser = oldUser.withName(newName);

        when(userGuard.checkIfExists(id)).thenReturn(oldUser);
        when(userDtoToUserMapper.mapFrom(dto)).thenReturn(newUser);
        when(userStorage.update(id, newUser)).thenReturn(newUser);

        assertThat(userService.update(dto)).isEqualTo(newUser);
    }

    @Test()
    void update_shouldThrowNotFoundException_ifStorageHasNoUser() {
        int id = 1;
        UserDto dto = getUserDto(id);

        when(userGuard.checkIfExists(id)).thenThrow(new NotFoundException("User", id));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.update(dto));
        assertThat(exception.getMessage()).isEqualTo("User with id=" + id + " not found");
    }

    @Test()
    void delete_shouldDeleteUserByIdAndReturnIt() {
        int id = 1;
        User user = getUser(id);

        when(userGuard.checkIfExists(id)).thenReturn(user);

        assertThat(userService.delete(id)).isEqualTo(user);
    }

    @Test()
    void delete_shouldThrowNotFoundException_ifStorageHasNoUser() {
        int id = 1;

        when(userGuard.checkIfExists(id)).thenThrow(new NotFoundException("User", id));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.delete(id));
        assertThat(exception.getMessage()).isEqualTo("User with id=" + id + " not found");
    }

    private User getUser(int id) {
        return new User(
                id,
                "test@test.test",
                "Test login",
                "Test name",
                LocalDate.EPOCH
        );
    }

    private UserDto getUserDto(int id) {
        return new UserDto(
                id,
                "test@test.test",
                "test_login",
                "Test name",
                LocalDate.EPOCH
        );
    }
}
