package ru.yandex.practicum.filmorate.film.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.film.model.Film;
import ru.yandex.practicum.filmorate.genre.model.Genre;
import ru.yandex.practicum.filmorate.mpa.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository("dbFilmStorage")
@RequiredArgsConstructor
public class DbFilmStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> getAll() {
        return jdbcTemplate.query(FilmQueries.getAll, this::mapRowsToFilms);
    }

    @Override
    public Film getById(int id) {
        try {
            return jdbcTemplate.query(FilmQueries.getById, this::mapRowsToFilm, id);
        } catch (DataAccessException exception) {
            return null;
        }
    }

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert filmSimpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("film")
                .usingGeneratedKeyColumns("film_id");

        Map<String, Object> filmColumns = new HashMap<>();
        filmColumns.put("name", film.getName());
        filmColumns.put("description", film.getDescription());
        filmColumns.put("release_date", film.getReleaseDate());
        filmColumns.put("duration", film.getDuration());
        filmColumns.put("mpa_id", film.getMpa().getId());

        int filmId = filmSimpleJdbcInsert.executeAndReturnKey(filmColumns).intValue();

        SimpleJdbcInsert genreSimpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("film_genre");

        if (film.getGenres() != null) {
            new HashSet<>(film.getGenres().stream().map(Genre::getId).collect(Collectors.toList())).forEach(genreId -> {
                Map<String, Object> genreColumns = new HashMap<>();
                genreColumns.put("film_id", filmId);
                genreColumns.put("genre_id", genreId);

                genreSimpleJdbcInsert.execute(genreColumns);
            });
        }

        return getById(filmId);
    }

    @Override
    public Film update(int id, Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("film_genre");

        jdbcTemplate.update(
                FilmQueries.update,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                id
        );

        jdbcTemplate.update(FilmQueries.deleteFilmGenres, film.getId());

        if (film.getGenres() != null) {
            new HashSet<>(film.getGenres().stream().map(Genre::getId).collect(Collectors.toList())).forEach(genreId -> {
                Map<String, Object> genreColumns = new HashMap<>();
                genreColumns.put("film_id", film.getId());
                genreColumns.put("genre_id", genreId);

                simpleJdbcInsert.execute(genreColumns);
            });
        }

        return getById(id);
    }

    @Override
    public Film delete(int id) {
        Film film = getById(id);

        if (film == null) {
            return null;
        }

        jdbcTemplate.update(FilmQueries.delete, id);

        return film;
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return jdbcTemplate.query(FilmQueries.getPopularFilms, this::mapRowsToFilms, Math.max(count, 0));
    }

    @Override
    public void likeFilm(int filmId, int userId) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("likes");
        Map<String, Object> likeColumns = new HashMap<>();
        likeColumns.put("film_id", filmId);
        likeColumns.put("user_id", userId);

        simpleJdbcInsert.execute(likeColumns);
    }

    @Override
    public void deleteLikeFromFilm(int filmId, int userId) {
        jdbcTemplate.update(FilmQueries.deleteFilmLike, filmId, userId);
    }

    private Film mapRowsToFilm(ResultSet resultSet) throws SQLException {
        Film film = null;

        while (resultSet.next()) {
            if (film == null) {
                film = new Film(
                        resultSet.getInt("film_id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getDate("release_date").toLocalDate(),
                        resultSet.getInt("duration"),
                        new Mpa(resultSet.getInt("mpa_id"), resultSet.getString("mpa_name")),
                        new ArrayList<>()
                );
            }

            Genre genre = new Genre(resultSet.getInt("genre_id"), resultSet.getString("genre_name"));

            if (genre.getId() != 0) {
                film.getGenres().add(genre);
            }
        }

        return film;
    }

    private List<Film> mapRowsToFilms(ResultSet resultSet) throws SQLException {
        Map<Integer, Film> films = new HashMap<>();

        while (resultSet.next()) {
            int id = resultSet.getInt("film_id");

            films.putIfAbsent(id, new Film(
                    resultSet.getInt("film_id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getDate("release_date").toLocalDate(),
                    resultSet.getInt("duration"),
                    new Mpa(resultSet.getInt("mpa_id"), resultSet.getString("mpa_name")),
                    new ArrayList<>()
            ));

            Genre genre = new Genre(resultSet.getInt("genre_id"), resultSet.getString("genre_name"));

            if (genre.getId() != 0) {
                films.get(id).getGenres().add(genre);
            }
        }

        return new ArrayList<>(films.values());
    }
}
