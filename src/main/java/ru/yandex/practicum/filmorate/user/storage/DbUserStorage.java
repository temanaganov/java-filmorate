package ru.yandex.practicum.filmorate.user.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.user.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("dbUserStorage")
@RequiredArgsConstructor
public class DbUserStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(UserQueries.GET_ALL, this::mapRowToUser);
    }

    @Override
    public User getById(int id) {
        try {
            return jdbcTemplate.queryForObject(UserQueries.GET_BY_ID, this::mapRowToUser, id);
        } catch (DataAccessException exception) {
            return null;
        }
    }

    @Override
    public User getByEmail(String email) {
        try {
            return jdbcTemplate.queryForObject(UserQueries.GET_BY_EMAIL, this::mapRowToUser, email);
        } catch (DataAccessException exception) {
            return null;
        }
    }

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");

        Map<String, Object> userColumns = new HashMap<>();
        userColumns.put("email", user.getEmail());
        userColumns.put("login", user.getLogin());
        userColumns.put("name", user.getName());
        userColumns.put("birthday", user.getBirthday());

        int userId = simpleJdbcInsert.executeAndReturnKey(userColumns).intValue();

        return getById(userId);
    }

    @Override
    public User update(int id, User user) {
        jdbcTemplate.update(
                UserQueries.update,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                id
        );

        return getById(id);
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(UserQueries.DELETE, id);
    }

    @Override
    public List<User> getFriends(int id) {
        return jdbcTemplate.query(UserQueries.GET_FRIENDS, this::mapRowToUser, id);
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherUserId) {
        return jdbcTemplate.query(UserQueries.GET_COMMON_FRIENDS, this::mapRowToUser, userId, otherUserId);
    }

    @Override
    public void addFriend(int userId, int friendId) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("friendship");

        Map<String, Object> userColumns = new HashMap<>();
        userColumns.put("user_id", userId);
        userColumns.put("friend_id", friendId);

        simpleJdbcInsert.execute(userColumns);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        jdbcTemplate.update(UserQueries.DELETE_FRIEND, userId, friendId);
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return new User(
                resultSet.getInt("user_id"),
                resultSet.getString("email"),
                resultSet.getString("login"),
                resultSet.getString("name"),
                resultSet.getDate("birthday").toLocalDate()
        );
    }
}
