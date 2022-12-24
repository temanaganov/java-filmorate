package ru.yandex.practicum.filmorate.user.storage;

import ru.yandex.practicum.filmorate.film.model.Film;
import ru.yandex.practicum.filmorate.user.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getAll();
    User getById(int id);
    User getByEmail(String email);
    User create(User user);
    User update(int id, User user);
    void delete(int id);
    void addFriend(int userId, int friendId);
    void deleteFriend(int userId, int friendId);
    List<User> getFriends(int id);
    List<User> getCommonFriends(int userId, int otherUserId);
    List<Film> getRecommendations(int userId);
}
