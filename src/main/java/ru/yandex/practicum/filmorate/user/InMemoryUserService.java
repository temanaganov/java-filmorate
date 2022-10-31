package ru.yandex.practicum.filmorate.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InMemoryUserService implements UserService {
    private int id = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(int id) {
        if (!users.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return users.get(id);
    }

    @Override
    public User createUser(User user) {
        String name = user.getName() == null || user.getName().isBlank() ? user.getLogin() : user.getName();
        User newUser = user.withId(getNextId()).withName(name);
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User deleteUser(int id) {
        if (!users.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        User user = users.get(id);
        users.remove(id);
        return user;
    }

    private int getNextId() {
        return ++id;
    }
}
