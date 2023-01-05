package ru.yandex.practicum.filmorate.repository.queries;

import ru.yandex.practicum.filmorate.model.film.FilmSort;

public class FilmQueries {
    public static final String GET_ALL = "SELECT * " +
            "FROM film AS f " +
            "JOIN mpa AS m ON f.mpa_id = m.mpa_id";

    public static String getByDirectorId(FilmSort sortBy) {
        StringBuilder sb = new StringBuilder("SELECT *, COUNT(*) AS likes " +
                "FROM film AS f " +
                "JOIN film_director AS fd ON f.film_id = fd.film_id " +
                "JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                "WHERE fd.director_id = ? " +
                "GROUP BY f.film_id "
        );

        if (sortBy != null) {
            sb.append("ORDER BY ").append(sortBy == FilmSort.year ? "f.release_date" : "likes");
        }

        return sb.toString();
    }

    public static final String SEARCH_BY_DIRECTOR = "SELECT f.*,m.* FROM film f " +
            "JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
            "LEFT JOIN film_director fd ON f.film_id = fd.film_id " +
            "LEFT JOIN director d ON fd.director_id = d.director_id " +
            "LEFT JOIN likes l ON f.film_id = l.film_id " +
            "WHERE LOWER(d.name) LIKE ? " +
            "GROUP BY f.film_id ORDER BY COUNT(l.film_id) DESC";

    public static final String SEARCH_BY_FILM = "SELECT f.*,m.* FROM film f " +
            "JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
            "LEFT JOIN likes l ON f.film_id = l.film_id " +
            "WHERE LOWER(f.name) LIKE ? " +
            "GROUP BY f.film_id ORDER BY COUNT(l.film_id) DESC";

    public static final String SEARCH_BY_FILM_OR_DIRECTOR = "SELECT f.*,m.* FROM film f " +
            "JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
            "LEFT JOIN film_director fd on f.film_id = fd.film_id " +
            "LEFT JOIN director d on fd.director_id = d.director_id " +
            "LEFT JOIN likes l on f.film_id = l.film_id " +
            "WHERE LOWER(f.name) LIKE ? OR LOWER(d.name) LIKE ? " +
            "GROUP BY f.film_id ORDER BY COUNT(l.film_id) DESC";

    public static final String SEARCH_NO_ARGS = "SELECT f.*,m.* FROM film f " +
            "JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
            "LEFT JOIN likes l on f.film_id = l.film_id " +
            "GROUP BY f.film_id ORDER BY COUNT(l.film_id) DESC";

    public static final String GET_BY_ID = "SELECT * " +
            "FROM film AS f " +
            "JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
            "WHERE f.film_id = ?";

    public static final String UPDATE = "UPDATE film " +
            "SET " +
            "name = ?, " +
            "description = ?, " +
            "release_date = ?, " +
            "duration = ?, " +
            "mpa_id = ? " +
            "WHERE film_id = ?";

    public static final String DELETE = "DELETE FROM film WHERE film_id = ?";

    public static String getPopularFilms(Integer genreId, Integer year) {
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

        sb.append("GROUP BY f.film_id, f.name, f.description, f.release_date, f.duration, l.user_id, m.mpa_id, m.name ")
                .append("ORDER BY likes DESC, f.name LIMIT ?");

        return sb.toString();
    }

    public static final String DELETE_FILM_GENRES = "DELETE FROM film_genre WHERE film_id = ?";

    public static final String DELETE_FILM_DIRECTORS = "DELETE FROM film_director WHERE film_id = ?";

    public static final String LIKE_FILM = "INSERT INTO likes VALUES(?, ?)";

    public static final String DELETE_LIKE_FROM_FILM = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";

    public static final String ADD_GENRE = "INSERT INTO film_genre VALUES(?, ?)";

    public static final String ADD_DIRECTOR = "INSERT INTO film_director VALUES(?, ?)";

    public static final String GET_COMMON_FILMS = "SELECT * " +
            "FROM film AS f " +
            "JOIN mpa AS m ON m.mpa_id = f.mpa_id " +
            "JOIN likes AS l1 ON (l1.film_id = f.film_id AND l1.user_id = ?) " +
            "JOIN likes AS l2 ON (l2.film_id = f.film_id AND l2.user_id = ?) " +
            "JOIN (SELECT film_id, COUNT(user_id) AS rate " +
            "FROM likes " +
            "GROUP BY film_id) AS fl ON (fl.film_id = f.film_id) " +
            "ORDER BY fl.rate DESC";
}
