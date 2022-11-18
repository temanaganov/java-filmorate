package ru.yandex.practicum.filmorate.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.core.exception.FieldValidationException;
import ru.yandex.practicum.filmorate.core.exception.NotFoundException;
import ru.yandex.practicum.filmorate.core.util.Mapper;
import ru.yandex.practicum.filmorate.user.dto.UserDto;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final Mapper<UserDto, User> userDtoToUserMapper;

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
        return userStorage.create(newUser);
    }

    @Override
    public User update(UserDto dto) {
        User currentUser = userStorage.getById(dto.getId());

        if (currentUser == null) {
            throw new NotFoundException("user", dto.getId());
        }

        User user = userDtoToUserMapper.mapFrom(dto).withFriends(currentUser.getFriends());

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
    public User addFriend(int userId, int friendId) {
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

        Set<Integer> currentUserFriends = new LinkedHashSet<>(user.getFriends());
        Set<Integer> currentFriendFriends = new LinkedHashSet<>(friend.getFriends());

        currentUserFriends.add(friendId);
        currentFriendFriends.add(userId);

        userStorage.update(friendId, friend.withFriends(currentFriendFriends));

        return userStorage.update(userId, user.withFriends(currentUserFriends));
    }

    @Override
    public User deleteFriend(int userId, int friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        if (user == null) {
            throw new NotFoundException("user", userId);
        }

        if (friend == null) {
            throw new NotFoundException("user", friendId);
        }

        Set<Integer> currentUserFriends = new LinkedHashSet<>(user.getFriends());
        Set<Integer> currentFriendFriends = new LinkedHashSet<>(friend.getFriends());

        currentUserFriends.remove(friendId);
        currentFriendFriends.remove(userId);

        userStorage.update(friendId, friend.withFriends(currentFriendFriends));

        return userStorage.update(userId, user.withFriends(currentUserFriends));
    }

    @Override
    public List<User> getFriends(int id) {
        User user = userStorage.getById(id);

        if (user == null) {
            throw new NotFoundException("user", id);
        }

        return user
                .getFriends()
                .stream()
                .map(userStorage::getById)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherId) {
        User user = userStorage.getById(userId);
        User otherUser = userStorage.getById(otherId);

        if (user == null) {
            throw new NotFoundException("user", userId);
        }

        if (otherUser == null) {
            throw new NotFoundException("user", otherId);
        }

        return user
                .getFriends()
                .stream()
                .filter(friendId -> otherUser.getFriends().contains(friendId))
                .map(userStorage::getById)
                .collect(Collectors.toList());
    }
}
