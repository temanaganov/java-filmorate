package ru.yandex.practicum.filmorate.film.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.director.model.Director;
import ru.yandex.practicum.filmorate.director.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.film.model.Film;
import ru.yandex.practicum.filmorate.genre.model.Genre;
import ru.yandex.practicum.filmorate.genre.storage.GenreStorage;
import ru.yandex.practicum.filmorate.mpa.model.Mpa;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository("dbFilmStorage")
@RequiredArgsConstructor
public class DbFilmStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;
    private final DirectorStorage directorStorage;

    @Override
    public List<Film> getAll() {
        return jdbcTemplate.query(FilmQueries.GET_ALL, this::mapRowToFilm);
    }

    @Override
    public List<Film> getAllFilmsByDirectorId(int directorId, String sortBy) {
        return jdbcTemplate.query(FilmQueries.GET_ALL_BY_DIRECTOR_ID(sortBy), this::mapRowToFilm, directorId);
    }

    @Override
    public Film getById(int id) {
        try {
            return jdbcTemplate.queryForObject(FilmQueries.GET_BY_ID, this::mapRowToFilm, id);
        } catch (DataAccessException exception) {
            return null;
        }
    }

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("film")
                .usingGeneratedKeyColumns("film_id");

        Map<String, Object> filmColumns = new HashMap<>();
        filmColumns.put("name", film.getName());
        filmColumns.put("description", film.getDescription());
        filmColumns.put("release_date", film.getReleaseDate());
        filmColumns.put("duration", film.getDuration());
        filmColumns.put("mpa_id", film.getMpa().getId());

        int filmId = simpleJdbcInsert.executeAndReturnKey(filmColumns).intValue();

        updateFilmGenres(film.getGenres(), filmId);
        updateFilmDirectors(film.getDirectors(), filmId);

        return getById(filmId);
    }

    @Override
    public Film update(int id, Film film) {
        jdbcTemplate.update(
                FilmQueries.UPDATE,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                id
        );

        jdbcTemplate.update(FilmQueries.DELETE_FILM_GENRES, film.getId());
        jdbcTemplate.update(FilmQueries.DELETE_FILM_DIRECTORS, film.getId());

        updateFilmGenres(film.getGenres(), id);
        updateFilmDirectors(film.getDirectors(), id);

        return getById(id);
    }

    @Override
    public Film delete(int id) {
        Film film = getById(id);

        if (film == null) {
            return null;
        }

        jdbcTemplate.update(FilmQueries.DELETE, id);

        return film;
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return jdbcTemplate.query(FilmQueries.GET_POPULAR_FILMS, this::mapRowToFilm, Math.max(count, 0));
    }

    @Override
    public void likeFilm(int filmId, int userId) {
        jdbcTemplate.update(FilmQueries.LIKE_FILM, filmId, userId);
    }

    @Override
    public void deleteLikeFromFilm(int filmId, int userId) {
        jdbcTemplate.update(FilmQueries.DELETE_LIKE_FROM_FILM, filmId, userId);
    }

    private void updateFilmGenres(List<Genre> genres, int filmId) {
        if (genres == null) {
            return;
        }

        List<Integer> genreUniqueIds = genres.stream()
                .map(Genre::getId)
                .distinct()
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate(
                FilmQueries.ADD_GENRE,
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        int genreId = genreUniqueIds.get(i);
                        ps.setInt(1, filmId);
                        ps.setInt(2, genreId);
                    }

                    public int getBatchSize() {
                        return genreUniqueIds.size();
                    }
                });
    }

    private void updateFilmDirectors(List<Director> directors, int filmId) {
        if (directors == null) {
            return;
        }

        List<Integer> directorUniqueIds = directors.stream()
                .map(Director::getId)
                .distinct()
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate(
                FilmQueries.ADD_DIRECTOR,
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        int directorId = directorUniqueIds.get(i);
                        ps.setInt(1, filmId);
                        ps.setInt(2, directorId);
                    }

                    public int getBatchSize() {
                        return directorUniqueIds.size();
                    }
                }
        );
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return new Film(
                resultSet.getInt("film_id"),
                resultSet.getString("film.name"),
                resultSet.getString("description"),
                resultSet.getDate("release_date").toLocalDate(),
                resultSet.getInt("duration"),
                new Mpa(resultSet.getInt("mpa.mpa_id"), resultSet.getString("mpa.name")),
                genreStorage.getAllByFilmId(resultSet.getInt("film_id")),
                directorStorage.getAllByFilmId(resultSet.getInt("film_id"))
        );
    }
}
