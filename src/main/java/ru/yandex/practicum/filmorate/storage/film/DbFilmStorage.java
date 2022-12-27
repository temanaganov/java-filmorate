package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.director.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.genre.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.model.mpa.Mpa;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class DbFilmStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;
    private final DirectorStorage directorStorage;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public DbFilmStorage(JdbcTemplate jdbcTemplate, GenreStorage genreStorage, DirectorStorage directorStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
        this.directorStorage = directorStorage;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("film")
                .usingGeneratedKeyColumns("film_id");
    }

    @Override
    public List<Film> getAll() {
        return jdbcTemplate.query(FilmQueries.GET_ALL, this::mapRowToFilm);
    }

    @Override
    public List<Film> getFilmsByDirectorId(int directorId, String sortBy) {
        return jdbcTemplate.query(FilmQueries.getAllByDirectorId(sortBy), this::mapRowToFilm, directorId);
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
    public void delete(int id) {
        jdbcTemplate.update(FilmQueries.DELETE, id);
    }

    @Override
    public List<Film> getPopularFilms(int count, Integer genreId, Integer year) {
        return jdbcTemplate.query(FilmQueries.getPopularFilms(genreId, year), this::mapRowToFilm, Math.max(count, 0));
    }

    @Override
    public void likeFilm(int filmId, int userId) {
        jdbcTemplate.update(FilmQueries.LIKE_FILM, filmId, userId);
    }

    @Override
    public void deleteLikeFromFilm(int filmId, int userId) {
        jdbcTemplate.update(FilmQueries.DELETE_LIKE_FROM_FILM, filmId, userId);
    }

    @Override
    public List<Film> getCommonFilms(int userId, int friendId) {
        return jdbcTemplate.query(FilmQueries.GET_COMMON_FILMS, this::mapRowToFilm, userId, friendId);
    }

    @Override
    public List<Film> search(String query, String by) {
        query = "%" + query.toLowerCase() + "%";
        String[] byList = by.split(",");
        if (byList.length != 0) {
            if (byList.length == 1) {
                if (byList[0].equals("director")) {
                    return jdbcTemplate.query(FilmQueries.SEARCH_BY_DIRECTOR, this::mapRowToFilm, query);
                } else {
                    return jdbcTemplate.query(FilmQueries.SEARCH_BY_FILM, this::mapRowToFilm, query);
                }
            } else {
                return jdbcTemplate.query(FilmQueries.SEARCH_BY_FILM_OR_DIRECTOR, this::mapRowToFilm, query, query);
            }
        } else {
            return jdbcTemplate.query(FilmQueries.SEARCH_NO_ARGS, this::mapRowToFilm);
        }
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
        return Film.builder()
                .id(resultSet.getInt("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(new Mpa(resultSet.getInt("mpa_id"), resultSet.getString("mpa.name")))
                .genres(genreStorage.getAllByFilmId(resultSet.getInt("film_id")))
                .directors(directorStorage.getAllByFilmId(resultSet.getInt("film_id")))
                .build();
    }
}
