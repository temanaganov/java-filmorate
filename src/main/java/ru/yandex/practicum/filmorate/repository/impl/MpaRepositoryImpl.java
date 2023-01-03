package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.MpaRepository;
import ru.yandex.practicum.filmorate.repository.queries.MpaQueries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MpaRepositoryImpl implements MpaRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAll() {
        return jdbcTemplate.query(MpaQueries.GET_ALL, this::mapRowToRate);
    }

    @Override
    public Mpa getById(int id) {
        try {
            return jdbcTemplate.queryForObject(MpaQueries.GET_BY_ID, this::mapRowToRate, id);
        } catch (DataAccessException exception) {
            return null;
        }
    }

    private Mpa mapRowToRate(ResultSet resultSet, int rowNum) throws SQLException {
        return new Mpa(resultSet.getInt("mpa_id"), resultSet.getString("name"));
    }
}
