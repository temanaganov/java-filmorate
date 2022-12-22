package ru.yandex.practicum.filmorate.film.storage;

public class FilmQueries {
    static final String GET_ALL = "SELECT * " +
            "FROM film AS f " +
            "JOIN mpa AS m ON f.mpa_id = m.mpa_id";

    static final String GET_BY_ID = "SELECT * " +
            "FROM film AS f " +
            "JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
            "WHERE f.film_id = ?";

    static final String UPDATE = "UPDATE film " +
            "SET " +
            "name = ?, " +
            "description = ?, " +
            "release_date = ?, " +
            "duration = ?, " +
            "mpa_id = ? " +
            "WHERE film_id = ?";

    static final String DELETE = "DELETE FROM film WHERE film_id = ?";

    static String GET_POPULAR_FILMS(Integer genreId, Integer year) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT *, ")
                .append("COUNT(l.film_id) AS likes ")
                .append("FROM film AS f ")
                .append("INNER JOIN mpa AS m ON f.mpa_id = m.mpa_id ")
                .append("LEFT JOIN likes AS l ON f.film_id = l.film_id ");

        if (genreId != null) {
            sb.append("JOIN film_genre AS fg ON (fg.film_id = f.film_id AND fg.genre_id = ").append(genreId).append(") ");
        }

        if (year != null) {
            sb.append("WHERE EXTRACT(YEAR from f.release_date) = ").append(year).append(" ");
        }

        sb.append("GROUP BY f.film_id, f.name, f.description, f.release_date, f.duration, m.mpa_id, m.name ")
                .append("ORDER BY likes DESC, f.name LIMIT ?");

        return sb.toString();
    }

    static final String DELETE_FILM_GENRES = "DELETE FROM film_genre WHERE film_id = ?";

    static final String LIKE_FILM = "INSERT INTO likes VALUES(?, ?)";

    static final String DELETE_LIKE_FROM_FILM = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";

    static final String ADD_GENRE = "INSERT INTO film_genre VALUES(?, ?)";
}
