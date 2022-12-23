package ru.yandex.practicum.filmorate.film.storage;

public class FilmQueries {
    static final String GET_ALL = "SELECT * " +
            "FROM film AS f " +
            "JOIN mpa AS m ON f.mpa_id = m.mpa_id";

    static String GET_ALL_BY_DIRECTOR_ID(String orderBy) {
        boolean isOrderByIsYearOrLikes = orderBy.equals("year") || orderBy.equals("likes");
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT *, COUNT(*) AS likes ")
                .append("FROM film AS f ")
                .append("JOIN film_director AS fd ON f.film_id = fd.film_id ")
                .append("JOIN mpa AS m ON f.mpa_id = m.mpa_id ")
                .append("LEFT JOIN likes AS l ON f.film_id = l.film_id ")
                .append("WHERE fd.director_id = ? ")
                .append("GROUP BY f.film_id ");

        if (isOrderByIsYearOrLikes) {
            sb.append("ORDER BY ").append(orderBy.equals("year") ? "f.release_date" : "likes");
        }

        return sb.toString();
    }

    static final String SEARCH_BY_DIRECTOR = "select * from film f " +
            "JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
            "left join film_director fd on f.film_id=fd.film_id " +
            "left join director d on fd.director_id=d.director_id " +
            "left join likes l on f.film_id=l.film_id " +
            "where lower(d.name) like ?" +
            "group by f.film_id order by count(l.film_id) desc";
    static final String SEARCH_BY_FILM = "select * from film f " +
            "JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
            "left join likes l on f.film_id=l.film_id " +
            "where lower(f.name) like ? " +
            "group by f.film_id order by count(l.film_id) desc";
    static final String SEARCH_BY_FILM_OR_DIRECTOR = "select * from film f " +
            "JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
            "left join film_director fd on f.film_id=fd.film_id " +
            "left join director d on fd.director_id=d.director_id " +
            "left join likes l on f.film_id=l.film_id " +
            "where lower(f.name) like ? or lower(d.name) like ?" +
            "group by f.film_id order by count(l.film_id) desc";
    static final String SEARCH_NO_ARGS = "select * from film f " +
            "left join likes l on f.film_id=l.film_id " +
            "group by f.film_id order by count(l.film_id) desc";
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

    static final String GET_POPULAR_FILMS = "SELECT " +
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

    static final String DELETE_FILM_GENRES = "DELETE FROM film_genre WHERE film_id = ?";

    static final String DELETE_FILM_DIRECTORS = "DELETE FROM film_director WHERE film_id = ?";

    static final String LIKE_FILM = "INSERT INTO likes VALUES(?, ?)";

    static final String DELETE_LIKE_FROM_FILM = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";

    static final String ADD_GENRE = "INSERT INTO film_genre VALUES(?, ?)";

    static final String ADD_DIRECTOR = "INSERT INTO film_director VALUES(?, ?)";
}
