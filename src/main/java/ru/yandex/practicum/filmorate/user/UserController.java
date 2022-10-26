package ru.yandex.practicum.filmorate.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService = new InMemoryUserService();

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") int id) {
        User user = userService.getUser(id);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return user;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        validateUserLogin(user.getLogin());
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        validateUserLogin(user.getLogin());
        User updatedUser = userService.updateUser(user.getId(), user);

        if (updatedUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return updatedUser;
    }

    @DeleteMapping("/{id}")
    public User deleteUser(@PathVariable("id") int id) {
        User deleteUser = userService.deleteUser(id);

        if (deleteUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return deleteUser;
    }

    private void validateUserLogin(String login) {
        if (login.contains(" ")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
