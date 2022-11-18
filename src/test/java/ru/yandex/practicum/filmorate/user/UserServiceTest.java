package ru.yandex.practicum.filmorate.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.core.exception.FieldValidationException;
import ru.yandex.practicum.filmorate.core.exception.NotFoundException;
import ru.yandex.practicum.filmorate.user.dto.UserDto;
import ru.yandex.practicum.filmorate.user.dto.UserDtoToUserMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserStorage userStorage;

    @Mock
    UserDtoToUserMapper userDtoToUserMapper;

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

        when(userStorage.getById(id)).thenReturn(user);

        User resultUser = userService.getById(id);

        assertThat(resultUser).isEqualTo(user);
        verify(userStorage).getById(id);
    }

    @Test()
    void getById_shouldThrowNotFoundException_ifStorageHasNoUser() {
        int id = 1;
        when(userStorage.getById(id)).thenReturn(null);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.getById(id));
        assertThat(exception.getMessage()).isEqualTo("user with id=" + id + " not found");
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

        when(userStorage.getById(id)).thenReturn(oldUser);
        when(userDtoToUserMapper.mapFrom(dto)).thenReturn(newUser);
        when(userStorage.update(id, newUser)).thenReturn(newUser);

        assertThat(userService.update(dto)).isEqualTo(newUser);
    }

    @Test()
    void update_shouldThrowNotFoundException_ifStorageHasNoUser() {
        int id = 1;
        UserDto dto = getUserDto(id);

        when(userStorage.getById(id)).thenReturn(null);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.update(dto));
        assertThat(exception.getMessage()).isEqualTo("user with id=" + id + " not found");
    }

    @Test()
    void delete_shouldDeleteUserByIdAndReturnIt() {
        int id = 1;
        User user = getUser(id);

        when(userStorage.delete(id)).thenReturn(user);

        assertThat(userService.delete(id)).isEqualTo(user);
    }

    @Test()
    void delete_shouldThrowNotFoundException_ifStorageHasNoUser() {
        int id = 1;

        when(userStorage.delete(id)).thenReturn(null);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.delete(id));
        assertThat(exception.getMessage()).isEqualTo("user with id=" + id + " not found");
    }

    @Test
    void addFriend_shouldAddTwoUsersToEachOtherAsFriendsAndReturnUpdatedFirstUser() {
        int user1Id = 1;
        int user2Id = 2;
        User oldUser1 = getUser(user1Id);
        User oldUser2 = getUser(user2Id);
        User newUser1 = oldUser1.withFriends(Set.of(user2Id));
        User newUser2 = oldUser2.withFriends(Set.of(user1Id));

        when(userStorage.getById(user1Id)).thenReturn(oldUser1);
        when(userStorage.getById(user2Id)).thenReturn(oldUser2);
        when(userStorage.update(user1Id, newUser1)).thenReturn(newUser1);
        when(userStorage.update(user2Id, newUser2)).thenReturn(newUser2);

        assertThat(userService.addFriend(user1Id, user2Id)).isEqualTo(newUser1);
    }

    @Test
    void addFriend_shouldNotChangeUsers_ifTheyAreAlreadyFriendsAndReturnFirstUser() {
        int user1Id = 1;
        int user2Id = 2;
        User user1 = getUser(user1Id).withFriends(Set.of(user2Id));
        User user2 = getUser(user2Id).withFriends(Set.of(user1Id));

        when(userStorage.getById(user1Id)).thenReturn(user1);
        when(userStorage.getById(user2Id)).thenReturn(user2);
        when(userStorage.update(user1Id, user1)).thenReturn(user1);
        when(userStorage.update(user2Id, user2)).thenReturn(user2);

        assertThat(userService.addFriend(user1Id, user2Id)).isEqualTo(user1);
    }

    @Test
    void addFriend_shouldThrowFieldValidationException_ifUser1IdEqualsToUser2Id() {
        int userId = 1;
        assertThrows(FieldValidationException.class, () -> userService.addFriend(userId, userId));
    }

    @Test
    void addFriend_shouldThrowNotFoundException_ifStorageHasNoUser1() {
        int user1Id = 1;
        int user2Id = 2;
        User user2 = getUser(user2Id);

        when(userStorage.getById(user1Id)).thenReturn(null);
        when(userStorage.getById(user2Id)).thenReturn(user2);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.addFriend(user1Id, user2Id));
        assertThat(exception.getMessage()).isEqualTo("user with id=" + user1Id + " not found");
    }

    @Test
    void addFriend_shouldThrowNotFoundException_ifStorageHasNoUser2() {
        int user1Id = 1;
        int user2Id = 2;
        User user1 = getUser(user1Id);

        when(userStorage.getById(user1Id)).thenReturn(user1);
        when(userStorage.getById(user2Id)).thenReturn(null);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.addFriend(user1Id, user2Id));
        assertThat(exception.getMessage()).isEqualTo("user with id=" + user2Id + " not found");
    }

    @Test
    void deleteFriend_shouldRemoveTwoUsersFromEachOthersFriendsAndReturnFirstUser() {
        int user1Id = 1;
        int user2Id = 2;
        User oldUser1 = getUser(user1Id).withFriends(Set.of(user2Id));
        User oldUser2 = getUser(user2Id).withFriends(Set.of(user1Id));
        User newUser1 = oldUser1.withFriends(Collections.emptySet());
        User newUser2 = oldUser2.withFriends(Collections.emptySet());

        when(userStorage.getById(user1Id)).thenReturn(oldUser1);
        when(userStorage.getById(user2Id)).thenReturn(oldUser2);
        when(userStorage.update(user1Id, newUser1)).thenReturn(newUser1);
        when(userStorage.update(user2Id, newUser2)).thenReturn(newUser2);

        assertThat(userService.deleteFriend(user1Id, user2Id)).isEqualTo(newUser1);
    }

    @Test
    void deleteFriend_shouldNotChangeUsers_ifTheyHaveNotEachOtherInFriendsAndReturnFirstUser() {
        int user1Id = 1;
        int user2Id = 2;
        User user1 = getUser(user1Id);
        User user2 = getUser(user2Id);

        when(userStorage.getById(user1Id)).thenReturn(user1);
        when(userStorage.getById(user2Id)).thenReturn(user2);
        when(userStorage.update(user1Id, user1)).thenReturn(user1);
        when(userStorage.update(user2Id, user2)).thenReturn(user2);

        assertThat(userService.deleteFriend(user1Id, user2Id)).isEqualTo(user1);
    }

    @Test
    void deleteFriend_shouldThrowNotFoundException_ifStorageHasNoUser1() {
        int user1Id = 1;
        int user2Id = 2;
        User user2 = getUser(user2Id);

        when(userStorage.getById(user1Id)).thenReturn(null);
        when(userStorage.getById(user2Id)).thenReturn(user2);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.deleteFriend(user1Id, user2Id));
        assertThat(exception.getMessage()).isEqualTo("user with id=" + user1Id + " not found");
    }

    @Test
    void deleteFriend_shouldThrowNotFoundException_ifStorageHasNoUser2() {
        int user1Id = 1;
        int user2Id = 2;
        User user1 = getUser(user1Id);

        when(userStorage.getById(user1Id)).thenReturn(user1);
        when(userStorage.getById(user2Id)).thenReturn(null);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.deleteFriend(user1Id, user2Id));
        assertThat(exception.getMessage()).isEqualTo("user with id=" + user2Id + " not found");
    }

    @Test
    void getFriends_shouldReturnEmptyList_ifUserHasNoFriends() {
        int userId = 1;
        User user = getUser(userId);

        when(userStorage.getById(userId)).thenReturn(user);

        assertThat(userService.getFriends(userId)).isEqualTo(Collections.emptyList());
    }

    @Test
    void getFriends_shouldReturnTwoFriends() {
        int userId = 1;
        int friend1Id = 2;
        int friend2Id = 3;
        Set<Integer> friends = new LinkedHashSet<>(List.of(friend1Id, friend2Id));
        User user = getUser(userId).withFriends(friends);
        User friend1 = getUser(friend1Id).withFriends(Set.of(userId));
        User friend2 = getUser(friend2Id).withFriends(Set.of(userId));

        when(userStorage.getById(userId)).thenReturn(user);
        when(userStorage.getById(friend1Id)).thenReturn(friend1);
        when(userStorage.getById(friend2Id)).thenReturn(friend2);

        assertThat(userService.getFriends(userId)).isEqualTo(List.of(friend1, friend2));
    }

    @Test
    void getFriends_shouldThrowNotFoundException_ifStorageHasNoUser() {
        int userId = 1;

        when(userStorage.getById(userId)).thenReturn(null);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.getFriends(userId));
        assertThat(exception.getMessage()).isEqualTo("user with id=" + userId + " not found");
    }

    @Test
    void getCommonFriends_shouldReturnCommonFriendOfTwoUsers() {
        int user1Id = 1;
        int user2Id = 2;
        int user3Id = 3;
        User user1 = getUser(user1Id).withFriends(Set.of(user2Id, user3Id));
        User user2 = getUser(user2Id).withFriends(Set.of(user1Id, user3Id));
        User user3 = getUser(user3Id).withFriends(Set.of(user1Id, user2Id));

        when(userStorage.getById(user1Id)).thenReturn(user1);
        when(userStorage.getById(user2Id)).thenReturn(user2);
        when(userStorage.getById(user3Id)).thenReturn(user3);

        assertThat(userService.getCommonFriends(user1Id, user2Id)).isEqualTo(List.of(user3));
    }

    @Test
    void getCommonFriends_shouldReturnEmptyList_ifUsersHaveNoCommonFriends() {
        int user1Id = 1;
        int user2Id = 2;
        User user1 = getUser(user1Id).withFriends(Set.of(user2Id));
        User user2 = getUser(user2Id).withFriends(Set.of(user1Id));

        when(userStorage.getById(user1Id)).thenReturn(user1);
        when(userStorage.getById(user2Id)).thenReturn(user2);

        assertThat(userService.getCommonFriends(user1Id, user2Id)).isEqualTo(Collections.emptyList());
    }

    @Test
    void getCommonFriends_shouldReturnEmptyList_ifUsersHaveNoFriends() {
        int user1Id = 1;
        int user2Id = 2;
        User user1 = getUser(user1Id);
        User user2 = getUser(user2Id);

        when(userStorage.getById(user1Id)).thenReturn(user1);
        when(userStorage.getById(user2Id)).thenReturn(user2);

        assertThat(userService.getCommonFriends(user1Id, user2Id)).isEqualTo(Collections.emptyList());
    }

    @Test
    void getCommonFriends_shouldThrowNotFoundException_ifStorageHasNoUser1() {
        int user1Id = 1;
        int user2Id = 2;
        User user2 = getUser(user2Id);

        when(userStorage.getById(user1Id)).thenReturn(null);
        when(userStorage.getById(user2Id)).thenReturn(user2);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.getCommonFriends(user1Id, user2Id));
        assertThat(exception.getMessage()).isEqualTo("user with id=" + user1Id + " not found");
    }

    @Test
    void getCommonFriends_shouldThrowNotFoundException_ifStorageHasNoUser2() {
        int user1Id = 1;
        int user2Id = 2;
        User user1 = getUser(user1Id);

        when(userStorage.getById(user1Id)).thenReturn(user1);
        when(userStorage.getById(user2Id)).thenReturn(null);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.getCommonFriends(user1Id, user2Id));
        assertThat(exception.getMessage()).isEqualTo("user with id=" + user2Id + " not found");
    }

    private User getUser(int id) {
        return new User(
                id,
                "test@test.test",
                "Test login",
                "Test name",
                LocalDate.EPOCH,
                Collections.emptySet()
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
