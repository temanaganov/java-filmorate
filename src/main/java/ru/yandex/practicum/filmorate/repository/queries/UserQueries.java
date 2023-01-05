package ru.yandex.practicum.filmorate.repository.queries;

public class UserQueries {
    public static final String GET_ALL = "SELECT * FROM users";

    public static final String GET_BY_ID = "SELECT * FROM users WHERE user_id = ?";

    public static final String GET_BY_EMAIL = "SELECT * FROM users WHERE email = ?";

    public static final String update = "UPDATE users " +
            "SET " +
            "email = ?, " +
            "login = ?, " +
            "name = ?, " +
            "birthday = ? " +
            "WHERE user_id = ?";

    public static final String DELETE = "DELETE FROM users WHERE user_id = ?";

    public static final String GET_FRIENDS = "SELECT " +
            "u.user_id, " +
            "u.email, " +
            "u.login, " +
            "u.name, " +
            "u.birthday " +
            "FROM friendship AS f " +
            "JOIN users AS u on u.user_id = f.friend_id " +
            "WHERE f.user_id = ?";

    public static final String GET_COMMON_FRIENDS = "SELECT " +
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

    public static final String DELETE_FRIEND = "DELETE " +
            "FROM friendship " +
            "WHERE " +
            "user_id = ? AND friend_id = ? ";

    public static final String GET_USERS_FROM_LIKES = "SELECT l.user_id " +
            "FROM likes l " +
            "WHERE l.film_id IN " +
            "(SELECT film_id " +
            "FROM likes l1 " +
            "WHERE user_id = ?) and l.USER_ID <> ?" +
            "GROUP BY l.user_id " +
            "ORDER BY COUNT(l.film_id) " +
            "limit 1";

    public static final String GET_FILMS_FROM_LIKES = "SELECT * " +
            "FROM film as f " +
            "JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
            "JOIN likes AS l ON f.film_id = l.film_id " +
            "WHERE user_id = ?";
}
