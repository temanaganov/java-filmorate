package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dto.UserDto;

import java.util.List;

public interface UserService {
    List<User> getAll();
    User getById(int id);
    User create(UserDto dto);
    User update(UserDto dto);
    User delete(int id);
    void addFriend(int userId, int friendId);
    void deleteFriend(int userId, int friendId);
    List<User> getFriends(int id);
    List<User> getCommonFriends(int userId, int otherUserId);
    List<Film> getRecommendations(int userId);
}
