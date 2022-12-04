package ru.yandex.practicum.filmorate.genre.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.genre.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DbGenreStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAll() {
        String sql = "SELECT * FROM genre ORDER BY genre_id";
        try {
            return jdbcTemplate.query(sql, this::mapRowToGenre);
        } catch (DataAccessException exception) {
            return null;
        }
    }

    @Override
    public Genre getById(int id) {
        String sql = "SELECT * FROM genre WHERE genre_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToGenre, id);
        } catch (DataAccessException exception) {
            return null;
        }
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(resultSet.getInt("genre_id"), resultSet.getString("name"));
    }
}
