package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.repository.BeanPropertySqlParameterSource;
import ru.yandex.practicum.filmorate.repository.DirectorRepository;
import ru.yandex.practicum.filmorate.repository.queries.DirectorQueries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DirectorRepositoryImpl implements DirectorRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Director> getAll() {
        return jdbcTemplate.query(DirectorQueries.GET_ALL, this::mapRowToDirector);
    }

    @Override
    public List<Director> getByFilmId(int filmId) {
        try {
            return jdbcTemplate.query(DirectorQueries.GET_BY_FILM_ID, this::mapRowToDirector, filmId);
        } catch (DataAccessException exception) {
            return null;
        }
    }

    @Override
    public Director getById(int id) {
        try {
            return jdbcTemplate.queryForObject(DirectorQueries.GET_BY_ID, this::mapRowToDirector, id);
        } catch (DataAccessException exception) {
            return null;
        }
    }

    @Override
    public Director create(Director director) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("director")
                .usingGeneratedKeyColumns("director_id");

        SqlParameterSource parameters = new BeanPropertySqlParameterSource(director);
        int directorId = simpleJdbcInsert.executeAndReturnKey(parameters).intValue();

        return getById(directorId);
    }

    @Override
    public Director update(Director director) {
        jdbcTemplate.update(DirectorQueries.UPDATE, director.getName(), director.getId());

        return getById(director.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DirectorQueries.DELETE, id);
    }

    private Director mapRowToDirector(ResultSet resultSet, int rowNum) throws SQLException {
        return new Director(resultSet.getInt("director_id"), resultSet.getString("name"));
    }
}
