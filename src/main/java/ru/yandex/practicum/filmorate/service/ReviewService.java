package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.ReviewDto;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewService {
    List<Review> getAll(Integer filmId, int count);
    Review getById(int id);
    Review create(ReviewDto dto);
    Review update(ReviewDto dto);
    Review delete(int id);
    void like(int id, int userId);
    void dislike(int id, int userId);
    void deleteLike(int id, int userId);
    void deleteDislike(int id, int userId);
}
