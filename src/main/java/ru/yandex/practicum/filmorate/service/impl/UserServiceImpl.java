package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FieldValidationException;
import ru.yandex.practicum.filmorate.mapper.Mapper;
import ru.yandex.practicum.filmorate.model.event.EventOperation;
import ru.yandex.practicum.filmorate.model.event.EventType;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.guard.UserGuard;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final Mapper<UserDto, User> userDtoToUserMapper;
    private final EventService eventService;
    private final UserGuard userGuard;

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
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

        return userRepository.create(newUser);
    }

    @Override
    public User update(UserDto dto) {
        userGuard.checkIfExists(dto.getId());
        User user = userDtoToUserMapper.mapFrom(dto);

        if (emailIsBusy(user.getEmail())) {
            throw new FieldValidationException("email", "User with this email is already exists");
        }

        return userRepository.update(user);
    }

    @Override
    public User delete(int id) {
        User user = userGuard.checkIfExists(id);
        userRepository.delete(id);

        return user;
    }

    @Override
    public void addFriend(int userId, int friendId) {
        if (userId == friendId) {
            throw new FieldValidationException("friendId", "friendId can not be equal to userId");
        }

        userGuard.checkIfExists(userId);
        userGuard.checkIfExists(friendId);

        userRepository.addFriend(userId, friendId);
        eventService.createEvent(userId, EventType.FRIEND, EventOperation.ADD, friendId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        userGuard.checkIfExists(userId);
        userGuard.checkIfExists(friendId);

        userRepository.deleteFriend(userId, friendId);
        eventService.createEvent(userId, EventType.FRIEND, EventOperation.REMOVE, friendId);
    }

    @Override
    public List<User> getFriends(int id) {
        userGuard.checkIfExists(id);

        return userRepository.getFriends(id);
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherUserId) {
        userGuard.checkIfExists(userId);
        userGuard.checkIfExists(otherUserId);

        return userRepository.getCommonFriends(userId, otherUserId);
    }

    public List<Film> getRecommendations(int userId) {
        userGuard.checkIfExists(userId);

        return userRepository.getRecommendations(userId);
    }

    private boolean emailIsBusy(String email) {
        return userRepository.getByEmail(email) != null;
    }
}
