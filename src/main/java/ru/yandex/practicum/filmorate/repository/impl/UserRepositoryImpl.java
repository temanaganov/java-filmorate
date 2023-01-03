package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.repository.DirectorRepository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final GenreRepository genreRepository;
    private final DirectorRepository directorRepository;

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
    public User update(User user) {
        jdbcTemplate.update(
                UserQueries.update,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );

        return getById(user.getId());
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

    @Override
    public List<Film> getRecommendations(int userId) {
        final List<Integer> userIds = jdbcTemplate.query(UserQueries.GET_USERS_FROM_LIKES, this::mapRowToUserId, userId, userId);

        if (userIds.isEmpty()) {
            return Collections.emptyList();
        }

        int id = userIds.get(0);
        List<Film> filmsOfOriginalUser = jdbcTemplate.query(UserQueries.GET_FILMS_FROM_LIKES, this::mapRowToFilm, userId);
        List<Film> filmsOfFoundUser = jdbcTemplate.query(UserQueries.GET_FILMS_FROM_LIKES, this::mapRowToFilm, id);
        filmsOfFoundUser.removeAll(filmsOfOriginalUser);

        return filmsOfFoundUser;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("user_id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }

    private int mapRowToUserId(ResultSet rs,int rowNum) throws SQLException {
        return rs.getInt("user_id");
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(new Mpa(resultSet.getInt("mpa_id"), resultSet.getString("mpa.name")))
                .genres(genreRepository.getAllByFilmId(resultSet.getInt("film_id")))
                .directors(directorRepository.getAllByFilmId(resultSet.getInt("film_id")))
                .build();
    }
}
