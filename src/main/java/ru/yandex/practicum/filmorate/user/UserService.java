package ru.yandex.practicum.filmorate.user;

import ru.yandex.practicum.filmorate.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<User> getAll();
    User getById(int id);
    User create(UserDto dto);
    User update(UserDto dto);
    User delete(int id);
    User addFriend(int userId, int friendId);
    User deleteFriend(int userId, int friendId);
    List<User> getFriends(int id);
    List<User> getCommonFriends(int userId, int otherId);
}
