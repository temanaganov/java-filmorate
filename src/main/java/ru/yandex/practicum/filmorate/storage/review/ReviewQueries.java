package ru.yandex.practicum.filmorate.storage.review;

public class ReviewQueries {
    static final String GET_BY_ID = "SELECT * FROM review WHERE review_id = ?";

    static final String GET_ALL = "SELECT * FROM review LIMIT ?";

    static final String GET_ALL_BY_FILM_ID = "SELECT * FROM review " +
            "WHERE film_id = ? " +
            "LIMIT ?";

    static final String UPDATE = "UPDATE review " +
            "SET " +
            "content = ?, " +
            "is_positive = ? " +
            "WHERE review_id = ?";

    static final String DELETE = "DELETE FROM review WHERE review_id = ?";

    static final String GET_USEFUL = "SELECT (COUNT(*) - (SELECT COUNT(*) " +
            "FROM review_user " +
            "WHERE review_id = ? " +
            "AND is_like = FALSE)) AS useful " +
            "FROM review_user " +
            "WHERE review_id = ? " +
            "AND is_like = TRUE";

    static final String LIKE = "INSERT INTO review_user(review_id, user_id, is_like) VALUES(?, ?, ?)";

    static final String DELETE_USER_LIKE = "DELETE FROM review_user WHERE (review_id = ? AND user_id = ? AND is_like = ?)";
}
