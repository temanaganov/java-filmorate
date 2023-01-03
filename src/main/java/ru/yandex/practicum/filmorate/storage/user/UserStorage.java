package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;

public interface UserStorage {
    List<User> getAll();
    User getById(int id);
    User getByEmail(String email);
    User create(User user);
    User update(User user);
    void delete(int id);
    void addFriend(int userId, int friendId);
    void deleteFriend(int userId, int friendId);
    List<User> getFriends(int id);
    List<User> getCommonFriends(int userId, int otherUserId);
    List<Film> getRecommendations(int userId);
}
