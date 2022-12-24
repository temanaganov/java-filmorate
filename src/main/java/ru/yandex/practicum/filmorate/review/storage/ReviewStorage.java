package ru.yandex.practicum.filmorate.review.storage;

import ru.yandex.practicum.filmorate.review.model.Review;

import java.util.List;

public interface ReviewStorage {
    List<Review> getAll(Integer filmId, int count);
    Review getById(int id);
    Review create(Review review);
    Review update(Review review);
    void delete(int id);
}
