package ru.yandex.practicum.filmorate.film.storage;

public class FilmQueries {
    static final String getAll = "SELECT " +
            "f.film_id, " +
            "f.name, " +
            "f.description, " +
            "f.release_date, " +
            "f.duration, " +
            "m.mpa_id, " +
            "m.name AS mpa_name, " +
            "g.genre_id, " +
            "g.name AS genre_name " +
            "FROM film AS f " +
            "JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
            "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
            "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id";

    static final String getById = "SELECT " +
            "f.film_id, " +
            "f.name, " +
            "f.description, " +
            "f.release_date, " +
            "f.duration, " +
            "m.mpa_id, " +
            "m.name AS mpa_name, " +
            "g.genre_id, " +
            "g.name AS genre_name " +
            "FROM film AS f " +
            "JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
            "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
            "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id " +
            "WHERE f.film_id = ?";

    static final String update = "UPDATE film " +
            "SET " +
            "name = ?, " +
            "description = ?, " +
            "release_date = ?, " +
            "duration = ?, " +
            "mpa_id = ? " +
            "WHERE film_id = ?";

    static final String delete = "DELETE FROM film WHERE film_id = ?";

    static final String getPopularFilms = "SELECT " +
            "f.film_id, " +
            "f.name, " +
            "f.description, " +
            "f.release_date, " +
            "f.duration, " +
            "m.mpa_id AS mpa_id, " +
            "m.name AS mpa_name, " +
            "g.genre_id, " +
            "g.name AS genre_name, " +
            "COUNT(l.film_id) AS likes " +
            "FROM film AS f " +
            "INNER JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
            "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
            "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id " +
            "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
            "GROUP BY f.film_id, f.name, f.description, f.release_date, f.duration, m.mpa_id, m.name, g.genre_id, g.name " +
            "ORDER BY likes DESC, f.name " +
            "LIMIT ?";

    static final String deleteFilmGenres = "DELETE FROM film_genre WHERE film_id = ?";
    static final String deleteFilmLike = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
}
