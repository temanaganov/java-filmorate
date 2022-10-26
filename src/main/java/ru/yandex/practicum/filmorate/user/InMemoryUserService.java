package ru.yandex.practicum.filmorate.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryUserService implements UserService {
    private int id = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public List<User> getAllUsers() {
        return users.values().stream().toList();
    }

    @Override
    public User getUser(int id) {
        return users.get(id);
    }

    @Override
    public User createUser(User user) {
        String name = user.getName() == null || user.getName().isBlank() ? user.getLogin() : user.getName();
        User newUser = new User(getNextId(), user.getEmail(), user.getLogin(), name, user.getBirthday());
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public User updateUser(int id, User user) {
        if (!users.containsKey(id)) {
            return null;
        }
        users.put(id, user);
        return user;
    }

    @Override
    public User deleteUser(int id) {
        User user = users.get(id);
        users.remove(id);
        return user;
    }

    private int getNextId() {
        return ++id;
    }
}
