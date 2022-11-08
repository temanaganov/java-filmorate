package ru.yandex.practicum.filmorate.user;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.core.exception.NotFoundException;
import ru.yandex.practicum.filmorate.core.util.Mapper;
import ru.yandex.practicum.filmorate.user.dto.CreateUserDto;
import ru.yandex.practicum.filmorate.user.dto.UpdateUserDto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InMemoryUserService implements UserService {
    private final UserStorage userStorage;
    private final Mapper<CreateUserDto, User> createUserDtoToUserMapper;
    private final Mapper<UpdateUserDto, User> updateUserDtoToUserMapper;

    public InMemoryUserService(
            UserStorage userStorage,
            Mapper<CreateUserDto, User> createUserDtoToUserMapper,
            Mapper<UpdateUserDto, User> updateUserDtoToUserMapper
    ) {
        this.userStorage = userStorage;
        this.createUserDtoToUserMapper = createUserDtoToUserMapper;
        this.updateUserDtoToUserMapper = updateUserDtoToUserMapper;
    }

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public User getById(int id) {
        User user = userStorage.getById(id);
        if (user == null) throw new NotFoundException("user", id);
        return user;
    }

    @Override
    public User create(CreateUserDto dto) {
        User newUser = createUserDtoToUserMapper.mapFrom(dto);
        return userStorage.create(newUser);
    }

    @Override
    public User update(UpdateUserDto dto) {
        User currentUser = userStorage.getById(dto.getId());

        if (currentUser == null) throw new NotFoundException("user", dto.getId());

        User user = updateUserDtoToUserMapper.mapFrom(dto).withFriends(currentUser.getFriends());

        return userStorage.update(user);
    }

    @Override
    public User delete(int id) {
        User user = userStorage.delete(id);
        if (user == null) throw new NotFoundException("user", id);
        return user;
    }

    @Override
    public User addFriend(int userId, int friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        if (user == null) throw new NotFoundException("user", userId);
        if (friend == null) throw new NotFoundException("user", friendId);

        Set<Integer> currentUserFriends = new HashSet<>(user.getFriends());
        Set<Integer> currentFriendFriends = new HashSet<>(friend.getFriends());

        currentUserFriends.add(friendId);
        currentFriendFriends.add(userId);

        userStorage.update(friend.withFriends(currentFriendFriends));

        return userStorage.update(user.withFriends(currentUserFriends));
    }

    @Override
    public User deleteFriend(int userId, int friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        if (user == null) throw new NotFoundException("user", userId);
        if (friend == null) throw new NotFoundException("user", friendId);

        Set<Integer> currentUserFriends = new HashSet<>(user.getFriends());
        Set<Integer> currentFriendFriends = new HashSet<>(friend.getFriends());

        currentUserFriends.remove(friendId);
        currentFriendFriends.remove(userId);

        userStorage.update(friend.withFriends(currentFriendFriends));

        return userStorage.update(user.withFriends(currentUserFriends));
    }

    @Override
    public List<User> getFriends(int id) {
        User user = userStorage.getById(id);
        if (user == null) throw new NotFoundException("user", id);
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

        if (user == null) throw new NotFoundException("user", userId);
        if (otherUser == null) throw new NotFoundException("user", otherId);

        return user
                .getFriends()
                .stream()
                .filter(friendId -> otherUser.getFriends().contains(friendId))
                .map(userStorage::getById)
                .collect(Collectors.toList());
    }
}
