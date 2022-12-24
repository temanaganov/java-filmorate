package ru.yandex.practicum.filmorate.review.storage;

public class ReviewQueries {
    static final String GET_BY_ID = "SELECT * FROM review WHERE review_id = ?";

    static final String GET_ALL = "SELECT * FROM review " +
            "ORDER BY useful DESC " +
            "LIMIT ?";

    static final String GET_ALL_BY_FILM_ID = "SELECT * FROM review " +
            "WHERE film_id = ? " +
            "ORDER BY useful DESC " +
            "LIMIT ?";

    static final String UPDATE = "UPDATE review " +
            "SET " +
            "content = ?, " +
            "is_positive = ?, " +
            "useful = ? " +
            "WHERE review_id = ?";

    static final String DELETE = "DELETE FROM review WHERE review_id = ?";

    static final String GET_REVIEW_USEFUL = "SELECT COUNT(*) - " +
            "(SELECT COUNT(*)" +
            "FROM review_useful" +
            "WHERE review_id = ? AND is_like = FALSE) AS useful" +
            "FROM review_useful" +
            "WHERE review_id = ? AND is_like = TRUE";
}
