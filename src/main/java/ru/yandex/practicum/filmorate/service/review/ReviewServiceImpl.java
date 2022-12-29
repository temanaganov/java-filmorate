package ru.yandex.practicum.filmorate.service.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.mapper.Mapper;
import ru.yandex.practicum.filmorate.model.event.EventOperation;
import ru.yandex.practicum.filmorate.model.event.EventType;
import ru.yandex.practicum.filmorate.service.event.EventService;
import ru.yandex.practicum.filmorate.guard.FilmGuard;
import ru.yandex.practicum.filmorate.model.review.ReviewDto;
import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.guard.ReviewGuard;
import ru.yandex.practicum.filmorate.guard.UserGuard;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewStorage reviewStorage;
    private final Mapper<ReviewDto, Review> reviewDtoToReviewMapper;
    private final EventService eventService;
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

        Review eventReview = reviewStorage.create(review);
        eventService.createEvent(eventReview.getUserId(), EventType.REVIEW, EventOperation.ADD, eventReview.getReviewId());

        return eventReview;
    }

    @Override
    public Review update(ReviewDto dto) {
        reviewGuard.checkIfExists(dto.getReviewId());
        Review review = reviewDtoToReviewMapper.mapFrom(dto);

        userGuard.checkIfExists(review.getUserId());
        filmGuard.checkIfExists(review.getFilmId());

        Review eventReview = reviewStorage.update(review);

        eventService.createEvent(eventReview.getUserId(), EventType.REVIEW, EventOperation.UPDATE, eventReview.getReviewId());

        return eventReview;
    }

    @Override
    public Review delete(int id) {
        Review review = reviewGuard.checkIfExists(id);
        eventService.createEvent(review.getUserId(), EventType.REVIEW, EventOperation.REMOVE, review.getReviewId());
        reviewStorage.delete(id);

        return review;
    }

    @Override
    public void like(int id, int userId) {
        reviewGuard.checkIfExists(id);
        userGuard.checkIfExists(userId);

        reviewStorage.deleteEstimation(id, userId, true);
        reviewStorage.deleteEstimation(id, userId, false);
        reviewStorage.estimate(id, userId, true);
    }

    @Override
    public void dislike(int id, int userId) {
        reviewGuard.checkIfExists(id);
        userGuard.checkIfExists(userId);

        reviewStorage.deleteEstimation(id, userId, true);
        reviewStorage.deleteEstimation(id, userId, false);
        reviewStorage.estimate(id, userId, false);
    }

    @Override
    public void deleteLike(int id, int userId) {
        reviewGuard.checkIfExists(id);
        userGuard.checkIfExists(userId);

        reviewStorage.deleteEstimation(id, userId, true);
        reviewStorage.estimate(id, userId, true);
    }

    @Override
    public void deleteDislike(int id, int userId) {
        reviewGuard.checkIfExists(id);
        userGuard.checkIfExists(userId);

        reviewStorage.deleteEstimation(id, userId, false);
        reviewStorage.estimate(id, userId, false);
    }
}
