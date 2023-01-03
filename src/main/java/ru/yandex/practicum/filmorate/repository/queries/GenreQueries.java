package ru.yandex.practicum.filmorate.repository.queries;

public class GenreQueries {
    public static final String GET_ALL = "SELECT * FROM genre ORDER BY genre_id";

    public static final String GET_BY_ID = "SELECT * FROM genre WHERE genre_id = ?";

    public static final String GET_BY_FILM_ID = "SELECT fg.genre_id AS genre_id, g.name AS name " +
            "FROM film_genre AS fg " +
            "JOIN genre AS g ON fg.genre_id = g.genre_id " +
            "WHERE fg.film_id = ? ";
}
