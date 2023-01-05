package ru.yandex.practicum.filmorate.repository.queries;

public class ReviewQueries {
    public static final String GET_ALL = "SELECT r.*, " +
            "(CASE WHEN ru.is_like IS NULL THEN 0 " +
            "ELSE SUM(CASE WHEN ru.is_like THEN 1 ELSE -1 END) " +
            "END) AS useful " +
            "FROM review AS r " +
            "LEFT JOIN review_user AS ru on r.review_id = ru.review_id " +
            "GROUP BY r.review_id " +
            "ORDER BY useful DESC " +
            "LIMIT ?";

    public static final String GET_BY_FILM_ID  = "SELECT r.*, " +
            "(CASE WHEN ru.is_like IS NULL THEN 0 " +
            "ELSE SUM(CASE WHEN ru.is_like THEN 1 ELSE -1 END) " +
            "END) AS useful " +
            "FROM review AS r " +
            "LEFT JOIN review_user AS ru on r.review_id = ru.review_id " +
            "WHERE film_id = ? " +
            "GROUP BY r.review_id " +
            "ORDER BY useful DESC " +
            "LIMIT ?";

    public static final String GET_BY_ID = "SELECT r.*, " +
            "(CASE WHEN ru.is_like IS NULL THEN 0 " +
            "ELSE SUM(CASE WHEN ru.is_like THEN 1 ELSE -1 END) " +
            "END) AS useful " +
            "FROM review AS r " +
            "LEFT JOIN review_user AS ru on r.review_id = ru.review_id " +
            "WHERE r.review_id = ? " +
            "GROUP BY r.review_id";

    public static final String UPDATE = "UPDATE review " +
            "SET " +
            "content = ?, " +
            "is_positive = ? " +
            "WHERE review_id = ?";

    public static final String DELETE = "DELETE FROM review WHERE review_id = ?";

    public static final String LIKE = "INSERT INTO review_user(review_id, user_id, is_like) VALUES(?, ?, ?)";

    public static final String DELETE_USER_LIKE = "DELETE FROM review_user WHERE (review_id = ? AND user_id = ? AND is_like = ?)";
}
