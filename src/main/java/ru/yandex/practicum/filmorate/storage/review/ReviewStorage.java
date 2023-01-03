package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {
    List<Review> getAll(Integer filmId, int count);
    Review getById(int id);
    Review create(Review review);
    Review update(Review review);
    void delete(int id);
    void estimate(int id, int userId, boolean isLike);
    void deleteEstimation(int id, int userId, boolean isLike);
}
