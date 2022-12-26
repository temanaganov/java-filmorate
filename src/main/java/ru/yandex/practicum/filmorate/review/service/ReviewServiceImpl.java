package ru.yandex.practicum.filmorate.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.core.util.Mapper;
import ru.yandex.practicum.filmorate.film.util.FilmGuard;
import ru.yandex.practicum.filmorate.review.dto.ReviewDto;
import ru.yandex.practicum.filmorate.review.model.Review;
import ru.yandex.practicum.filmorate.review.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.review.util.ReviewGuard;
import ru.yandex.practicum.filmorate.user.util.UserGuard;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewStorage reviewStorage;
    private final Mapper<ReviewDto, Review> reviewDtoToReviewMapper;
    private final ReviewGuard reviewGuard;
    private final UserGuard userGuard;
    private final FilmGuard filmGuard;

    @Override
    public List<Review> getAll(Integer filmId, int count) {
        return reviewStorage.getAll(filmId, count);
    }

    @Override
    public Review getById(int id) {
        return reviewGuard.checkIfExists(id);
    }

    @Override
    public Review create(ReviewDto dto) {
        Review review = reviewDtoToReviewMapper.mapFrom(dto);
        userGuard.checkIfExists(review.getUserId());
        filmGuard.checkIfExists(review.getFilmId());

        return reviewStorage.create(review);
    }

    @Override
    public Review update(ReviewDto dto) {
        Review currentReview = reviewGuard.checkIfExists(dto.getReviewId());
        Review review = reviewDtoToReviewMapper.mapFrom(dto);

        userGuard.checkIfExists(review.getUserId());
        filmGuard.checkIfExists(review.getFilmId());

        review.setUseful(currentReview.getUseful());

        return reviewStorage.update(review);
    }

    @Override
    public Review delete(int id) {
        Review review = reviewGuard.checkIfExists(id);
        reviewStorage.delete(id);

        return review;
    }

    @Override
    public void like(int id, int userId) {
        Review review = reviewGuard.checkIfExists(id);
        review.setUseful(review.getUseful() + 1);

        reviewStorage.update(review);
    }

    @Override
    public void dislike(int id, int userId) {
        Review review = reviewGuard.checkIfExists(id);
        review.setUseful(review.getUseful() - 1);

        reviewStorage.update(review);
    }

    @Override
    public void deleteLike(int id, int userId) {
        Review review = reviewGuard.checkIfExists(id);
        review.setUseful(review.getUseful() - 1);

        reviewStorage.update(review);
    }

    @Override
    public void deleteDislike(int id, int userId) {
        Review review = reviewGuard.checkIfExists(id);
        review.setUseful(review.getUseful() + 1);

        reviewStorage.update(review);
    }
}
