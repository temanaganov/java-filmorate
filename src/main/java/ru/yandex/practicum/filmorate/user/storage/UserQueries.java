package ru.yandex.practicum.filmorate.user.storage;

public class UserQueries {
    static final String getAll = "SELECT * FROM users";

    static final String getById = "SELECT * FROM users WHERE user_id = ?";

    static final String getByEmail = "SELECT * FROM users WHERE email = ?";

    static final String update = "UPDATE users " +
            "SET " +
            "email = ?, " +
            "login = ?, " +
            "name = ?, " +
            "birthday = ? " +
            "WHERE user_id = ?";

    static final String delete = "DELETE FROM users WHERE user_id = ?";

    static final String getFriends = "SELECT " +
            "u.user_id, " +
            "u.email, " +
            "u.login, " +
            "u.name, " +
            "u.birthday " +
            "FROM friendship AS f " +
            "JOIN users AS u on u.user_id = f.friend_id " +
            "WHERE f.user_id = ?";

    static final String getCommonFriends = "SELECT " +
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

    static final String deleteFriend = "DELETE " +
            "FROM friendship " +
            "WHERE " +
            "user_id = ? AND friend_id = ? ";
}
