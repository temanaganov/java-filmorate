package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.mapper.Mapper;
import ru.yandex.practicum.filmorate.model.event.EventOperation;
import ru.yandex.practicum.filmorate.model.event.EventType;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.guard.FilmGuard;
import ru.yandex.practicum.filmorate.dto.ReviewDto;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.repository.ReviewRepository;
import ru.yandex.practicum.filmorate.guard.ReviewGuard;
import ru.yandex.practicum.filmorate.guard.UserGuard;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final Mapper<ReviewDto, Review> reviewDtoToReviewMapper;
    private final EventService eventService;
    private final ReviewGuard reviewGuard;
    private final UserGuard userGuard;
    private final FilmGuard filmGuard;

    @Override
    public List<Review> getAll(Integer filmId, int count) {
        return reviewRepository.getAll(filmId, count);
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

        Review eventReview = reviewRepository.create(review);
        eventService.createEvent(eventReview.getUserId(), EventType.REVIEW, EventOperation.ADD, eventReview.getReviewId());

        return eventReview;
    }

    @Override
    public Review update(ReviewDto dto) {
        reviewGuard.checkIfExists(dto.getReviewId());
        Review review = reviewDtoToReviewMapper.mapFrom(dto);

        userGuard.checkIfExists(review.getUserId());
        filmGuard.checkIfExists(review.getFilmId());

        Review eventReview = reviewRepository.update(review);

        eventService.createEvent(eventReview.getUserId(), EventType.REVIEW, EventOperation.UPDATE, eventReview.getReviewId());

        return eventReview;
    }

    @Override
    public Review delete(int id) {
        Review review = reviewGuard.checkIfExists(id);
        eventService.createEvent(review.getUserId(), EventType.REVIEW, EventOperation.REMOVE, review.getReviewId());
        reviewRepository.delete(id);

        return review;
    }

    @Override
    public void like(int id, int userId) {
        reviewGuard.checkIfExists(id);
        userGuard.checkIfExists(userId);

        reviewRepository.deleteEstimation(id, userId, true);
        reviewRepository.deleteEstimation(id, userId, false);
        reviewRepository.estimate(id, userId, true);
    }

    @Override
    public void dislike(int id, int userId) {
        reviewGuard.checkIfExists(id);
        userGuard.checkIfExists(userId);

        reviewRepository.deleteEstimation(id, userId, true);
        reviewRepository.deleteEstimation(id, userId, false);
        reviewRepository.estimate(id, userId, false);
    }

    @Override
    public void deleteLike(int id, int userId) {
        reviewGuard.checkIfExists(id);
        userGuard.checkIfExists(userId);

        reviewRepository.deleteEstimation(id, userId, true);
        reviewRepository.estimate(id, userId, true);
    }

    @Override
    public void deleteDislike(int id, int userId) {
        reviewGuard.checkIfExists(id);
        userGuard.checkIfExists(userId);

        reviewRepository.deleteEstimation(id, userId, false);
        reviewRepository.estimate(id, userId, false);
    }
}
