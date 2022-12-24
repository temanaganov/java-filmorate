package ru.yandex.practicum.filmorate.user.storage;

public class UserQueries {
    static final String GET_ALL = "SELECT * FROM users";

    static final String GET_BY_ID = "SELECT * FROM users WHERE user_id = ?";

    static final String GET_BY_EMAIL = "SELECT * FROM users WHERE email = ?";

    static final String update = "UPDATE users " +
            "SET " +
            "email = ?, " +
            "login = ?, " +
            "name = ?, " +
            "birthday = ? " +
            "WHERE user_id = ?";

    static final String DELETE = "DELETE FROM users WHERE user_id = ?";

    static final String GET_FRIENDS = "SELECT " +
            "u.user_id, " +
            "u.email, " +
            "u.login, " +
            "u.name, " +
            "u.birthday " +
            "FROM friendship AS f " +
            "JOIN users AS u on u.user_id = f.friend_id " +
            "WHERE f.user_id = ?";

    static final String GET_COMMON_FRIENDS = "SELECT " +
            "u.user_id, " +
            "u.email, " +
            "u.login, " +
            "u.name, " +
            "u.birthday " +
            "FROM friendship AS f " +
            "JOIN users AS u on u.user_id = f.friend_id " +
            "WHERE f.user_id = ? AND " +
            "f.friend_id IN (" +
            "SELECT friend_id " +
            "FROM friendship " +
            "WHERE user_id = ?)";

    static final String DELETE_FRIEND = "DELETE " +
            "FROM friendship " +
            "WHERE " +
            "user_id = ? AND friend_id = ? ";

    static final String GET_USERS_FROM_LIKES = "SELECT l.user_id " +
            "FROM likes l " +
            "WHERE l.film_id IN " +
            "                     (SELECT film_id " +
            "                      FROM likes l1 " +
            "                      WHERE user_id = ?) and l.USER_ID <> ?" +
            "GROUP BY l.user_id " +
            "ORDER BY COUNT(l.film_id) " +
            "limit 1";

    static final String GET_FILMS_FROM_LIKES = "SELECT film_id " +
            "FROM likes " +
            "WHERE user_id = ?";;
}
