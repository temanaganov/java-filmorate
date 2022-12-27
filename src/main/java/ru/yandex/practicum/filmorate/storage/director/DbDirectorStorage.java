package ru.yandex.practicum.filmorate.storage.director;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.director.Director;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class DbDirectorStorage implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Director> getAll() {
        String sql = "SELECT * FROM director ORDER BY director_id";

        return jdbcTemplate.query(sql, this::mapRowToDirector);
    }

    @Override
    public List<Director> getAllByFilmId(int filmId) {
        String sql = "SELECT fd.director_id AS director_id, d.name AS name " +
                "FROM film_director AS fd " +
                "JOIN director AS d ON fd.director_id = d.director_id " +
                "WHERE fd.film_id = ?";
        try {
            return jdbcTemplate.query(sql, this::mapRowToDirector, filmId);
        } catch (DataAccessException exception) {
            return null;
        }
    }

    @Override
    public Director getById(int id) {
        String sql = "SELECT * FROM director WHERE director_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToDirector, id);
        } catch (DataAccessException exception) {
            return null;
        }
    }

    @Override
    public Director create(Director director) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("director")
                .usingGeneratedKeyColumns("director_id");

        Map<String, Object> directorColumns = new HashMap<>();
        directorColumns.put("name", director.getName());

        int directorId = simpleJdbcInsert.executeAndReturnKey(directorColumns).intValue();

        return getById(directorId);
    }

    @Override
    public Director update(Director director) {
        String sql = "UPDATE director SET name = ? WHERE director_id = ?";
        jdbcTemplate.update(sql, director.getName(), director.getId());

        return getById(director.getId());
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM director WHERE director_id = ?";
        jdbcTemplate.update(sql, id);
    }

    private Director mapRowToDirector(ResultSet resultSet, int rowNum) throws SQLException {
        return new Director(resultSet.getInt("director_id"), resultSet.getString("name"));
    }
}
