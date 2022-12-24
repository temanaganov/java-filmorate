package ru.yandex.practicum.filmorate.user.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.core.exception.FieldValidationException;
import ru.yandex.practicum.filmorate.core.exception.NotFoundException;
import ru.yandex.practicum.filmorate.core.util.Mapper;
import ru.yandex.practicum.filmorate.film.model.Film;
import ru.yandex.practicum.filmorate.user.model.User;
import ru.yandex.practicum.filmorate.user.storage.UserStorage;
import ru.yandex.practicum.filmorate.user.dto.UserDto;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final Mapper<UserDto, User> userDtoToUserMapper;

    public UserServiceImpl(
            @Qualifier("dbUserStorage") UserStorage userStorage,
            Mapper<UserDto, User> userDtoToUserMapper
    ) {
        this.userStorage = userStorage;
        this.userDtoToUserMapper = userDtoToUserMapper;
    }

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public User getById(int id) {
        User user = userStorage.getById(id);

        if (user == null) {
            throw new NotFoundException("user", id);
        }

        return user;
    }

    @Override
    public User create(UserDto dto) {
        User newUser = userDtoToUserMapper.mapFrom(dto);

        if (emailIsBusy(newUser.getEmail())) {
            throw new FieldValidationException("email", "User with this email is already exists");
        }

        return userStorage.create(newUser);
    }

    @Override
    public User update(UserDto dto) {
        User currentUser = userStorage.getById(dto.getId());

        if (currentUser == null) {
            throw new NotFoundException("user", dto.getId());
        }

        User user = userDtoToUserMapper.mapFrom(dto);

        if (emailIsBusy(user.getEmail())) {
            throw new FieldValidationException("email", "User with this email is already exists");
        }

        return userStorage.update(user.getId(), user);
    }

    @Override
    public User delete(int id) {
        User user = userStorage.delete(id);

        if (user == null) {
            throw new NotFoundException("user", id);
        }

        return user;
    }

    @Override
    public void addFriend(int userId, int friendId) {
        if (userId == friendId) {
            throw new FieldValidationException("friendId", "friendId can not be equal to userId");
        }

        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        if (user == null) {
            throw new NotFoundException("user", userId);
        }

        if (friend == null) {
            throw new NotFoundException("user", friendId);
        }

        userStorage.addFriend(userId, friendId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        if (user == null) {
            throw new NotFoundException("user", userId);
        }

        if (friend == null) {
            throw new NotFoundException("user", friendId);
        }

        userStorage.deleteFriend(userId, friendId);
    }

    @Override
    public List<User> getFriends(int id) {
        User user = userStorage.getById(id);

        if (user == null) {
            throw new NotFoundException("user", id);
        }

        return userStorage.getFriends(id);
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherUserId) {
        User user = userStorage.getById(userId);
        User otherUser = userStorage.getById(otherUserId);

        if (user == null) {
            throw new NotFoundException("user", userId);
        }

        if (otherUser == null) {
            throw new NotFoundException("user", otherUserId);
        }

        return userStorage.getCommonFriends(userId, otherUserId);
    }

    public List<Film> getRecommendations(int userId){
        User user = userStorage.getById(userId);

        if (user == null) {
            throw new NotFoundException("user", userId);
        }
        return userStorage.getRecommendations(userId);
    }

    private boolean emailIsBusy(String email) {
        return userStorage.getByEmail(email) != null;
    }
}
