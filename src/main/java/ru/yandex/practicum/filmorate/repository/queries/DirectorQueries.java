package ru.yandex.practicum.filmorate.repository.queries;

public class DirectorQueries {
    public static final String GET_ALL = "SELECT * FROM director ORDER BY director_id";

    public static final String GET_BY_FILM_ID = "SELECT fd.director_id AS director_id, d.name AS name " +
            "FROM film_director AS fd " +
            "JOIN director AS d ON fd.director_id = d.director_id " +
            "WHERE fd.film_id = ?";

    public static final String GET_BY_ID = "SELECT * FROM director WHERE director_id = ?";

    public static final String UPDATE = "UPDATE director SET name = ? WHERE director_id = ?";

    public static final String DELETE = "DELETE FROM director WHERE director_id = ?";
}
