package ru.yandex.practicum.filmorate.user.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.core.exception.FieldValidationException;
import ru.yandex.practicum.filmorate.core.util.Guard;
import ru.yandex.practicum.filmorate.core.util.Mapper;
import ru.yandex.practicum.filmorate.event.service.EventService;
import ru.yandex.practicum.filmorate.film.model.Film;
import ru.yandex.practicum.filmorate.user.model.User;
import ru.yandex.practicum.filmorate.user.storage.UserStorage;
import ru.yandex.practicum.filmorate.user.dto.UserDto;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final Mapper<UserDto, User> userDtoToUserMapper;
    private final Guard<User> userGuard;
    private final EventService eventService;

    public UserServiceImpl(
            @Qualifier("dbUserStorage") UserStorage userStorage,
            Mapper<UserDto, User> userDtoToUserMapper,
            EventService eventService
    ) {
        this.userStorage = userStorage;
        this.userDtoToUserMapper = userDtoToUserMapper;
        this.eventService = eventService;
        this.userGuard = new Guard<>(userStorage::getById, User.class);
    }

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public User getById(int id) {
        return userGuard.checkIfExists(id);
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
        userGuard.checkIfExists(dto.getId());
        User user = userDtoToUserMapper.mapFrom(dto);

        if (emailIsBusy(user.getEmail())) {
            throw new FieldValidationException("email", "User with this email is already exists");
        }

        return userStorage.update(user.getId(), user);
    }

    @Override
    public User delete(int id) {
        User user = userGuard.checkIfExists(id);
        userStorage.delete(id);

        return user;
    }

    @Override
    public void addFriend(int userId, int friendId) {
        if (userId == friendId) {
            throw new FieldValidationException("friendId", "friendId can not be equal to userId");
        }

        userGuard.checkIfExists(userId);
        userGuard.checkIfExists(friendId);
        userStorage.addFriend(userId, friendId);
        eventService.addFriendEvent(userId, friendId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        userGuard.checkIfExists(userId);
        userGuard.checkIfExists(friendId);
        eventService.deleteFriendEvent(userId, friendId);
        userStorage.deleteFriend(userId, friendId);
    }

    @Override
    public List<User> getFriends(int id) {
        userGuard.checkIfExists(id);

        return userStorage.getFriends(id);
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherUserId) {
        userGuard.checkIfExists(userId);
        userGuard.checkIfExists(otherUserId);

        return userStorage.getCommonFriends(userId, otherUserId);
    }

    public List<Film> getRecommendations(int userId) {
        userGuard.checkIfExists(userId);

        return userStorage.getRecommendations(userId);
    }

    private boolean emailIsBusy(String email) {
        return userStorage.getByEmail(email) != null;
    }
}
