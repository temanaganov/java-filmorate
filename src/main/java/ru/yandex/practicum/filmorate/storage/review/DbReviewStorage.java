package ru.yandex.practicum.filmorate.storage.review;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.review.Review;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class DbReviewStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Review> getAll(Integer filmId, int count) {
        if (filmId == null) {
            return jdbcTemplate.query(ReviewQueries.GET_ALL, this::mapRowToReview, count);
        }

        return jdbcTemplate.query(ReviewQueries.GET_ALL_BY_FILM_ID, this::mapRowToReview, filmId, count);
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

        Map<String, Object> reviewColumns = new HashMap<>();
        reviewColumns.put("content", review.getContent());
        reviewColumns.put("is_positive", review.isPositive());
        reviewColumns.put("user_id", review.getUserId());
        reviewColumns.put("film_id", review.getFilmId());
        reviewColumns.put("useful", review.getUseful());

        int reviewId = simpleJdbcInsert.executeAndReturnKey(reviewColumns).intValue();

        return getById(reviewId);
    }

    @Override
    public Review update(Review review) {
        jdbcTemplate.update(
                ReviewQueries.UPDATE,
                review.getContent(),
                review.isPositive(),
                review.getUseful(),
                review.getReviewId()
        );

        return getById(review.getReviewId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(ReviewQueries.DELETE, id);
    }

    private Review mapRowToReview(ResultSet resultSet, int i) throws SQLException {
        return new Review(
                resultSet.getInt("review_id"),
                resultSet.getString("content"),
                resultSet.getBoolean("is_positive"),
                resultSet.getInt("user_id"),
                resultSet.getInt("film_id"),
                resultSet.getInt("useful")
        );
    }
}
