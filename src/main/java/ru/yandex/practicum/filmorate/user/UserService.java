package ru.yandex.practicum.filmorate.user;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUser(int id);
    User createUser(User user);
    User updateUser(int id, User user);
    User deleteUser(int id);
}
