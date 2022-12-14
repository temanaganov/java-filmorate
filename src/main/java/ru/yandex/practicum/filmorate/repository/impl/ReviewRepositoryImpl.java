package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.repository.ReviewRepository;
import ru.yandex.practicum.filmorate.repository.queries.ReviewQueries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Review> getAll(Integer filmId, int count) {
        if (filmId == null) {
            return jdbcTemplate.query(ReviewQueries.GET_ALL, this::mapRowToReview, count);
        }

        return jdbcTemplate.query(ReviewQueries.GET_BY_FILM_ID, this::mapRowToReview, filmId, count);
    }

    @Override
    public Review getById(int id) {
        try {
            return jdbcTemplate.queryForObject(ReviewQueries.GET_BY_ID, this::mapRowToReview, id);
        } catch (DataAccessException exception) {
            return null;
        }
    }

    @Override
    public Review create(Review review) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("review")
                .usingGeneratedKeyColumns("review_id");

        SqlParameterSource parameters = new BeanPropertySqlParameterSource(review);
        int reviewId = simpleJdbcInsert.executeAndReturnKey(parameters).intValue();

        return getById(reviewId);
    }

    @Override
    public Review update(Review review) {
        jdbcTemplate.update(
                ReviewQueries.UPDATE,
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId()
        );

        return getById(review.getReviewId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(ReviewQueries.DELETE, id);
    }

    @Override
    public void estimate(int id, int userId, boolean isLike) {
        jdbcTemplate.update(ReviewQueries.LIKE, id, userId, isLike);
    }

    @Override
    public void deleteEstimation(int id, int userId, boolean isLike) {
        jdbcTemplate.update(ReviewQueries.DELETE_USER_LIKE, id, userId, isLike);
    }

    private Review mapRowToReview(ResultSet resultSet, int i) throws SQLException {
        return Review.builder()
                .reviewId(resultSet.getInt("review_id"))
                .content(resultSet.getString("content"))
                .isPositive(resultSet.getBoolean("is_positive"))
                .userId(resultSet.getInt("user_id"))
                .filmId(resultSet.getInt("film_id"))
                .useful(resultSet.getInt("useful"))
                .build();
    }
}
