package ru.yandex.practicum.filmorate.review.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.core.util.Guard;
import ru.yandex.practicum.filmorate.core.util.Mapper;
import ru.yandex.practicum.filmorate.event.service.EventService;
import ru.yandex.practicum.filmorate.film.model.Film;
import ru.yandex.practicum.filmorate.film.storage.FilmStorage;
import ru.yandex.practicum.filmorate.review.dto.ReviewDto;
import ru.yandex.practicum.filmorate.review.model.Review;
import ru.yandex.practicum.filmorate.review.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.user.model.User;
import ru.yandex.practicum.filmorate.user.storage.UserStorage;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewStorage reviewStorage;
    private final EventService eventService;
    private final Guard<Review> reviewGuard;
    private final Guard<User> userGuard;
    private final Guard<Film> filmGuard;
    private final Mapper<ReviewDto, Review> reviewDtoToReviewMapper;

    public ReviewServiceImpl(
            ReviewStorage reviewStorage,
            EventService eventService,
            @Qualifier("dbUserStorage") UserStorage userStorage,
            @Qualifier("dbFilmStorage") FilmStorage filmStorage,
            Mapper<ReviewDto, Review> reviewDtoToReviewMapper
    ) {
        this.reviewStorage = reviewStorage;
        this.eventService = eventService;
        this.reviewGuard = new Guard<>(reviewStorage::getById, Review.class);
        this.userGuard = new Guard<>(userStorage::getById, User.class);
        this.filmGuard = new Guard<>(filmStorage::getById, Film.class);
        this.reviewDtoToReviewMapper = reviewDtoToReviewMapper;
    }

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
        eventService.addReviewEvent(eventReview.getUserId(), eventReview.getReviewId());

        return eventReview;

    }

    @Override
    public Review update(ReviewDto dto) {
        Review currentReview = reviewGuard.checkIfExists(dto.getReviewId());
        Review review = reviewDtoToReviewMapper.mapFrom(dto);

        userGuard.checkIfExists(review.getUserId());
        filmGuard.checkIfExists(review.getFilmId());

        review.setUseful(currentReview.getUseful());
        Review eventReview = reviewStorage.update(review);

        eventService.updateReviewEvent(eventReview.getUserId(), eventReview.getReviewId());

        return eventReview;
    }

    @Override
    public Review delete(int id) {
        Review review = reviewGuard.checkIfExists(id);
        eventService.deleteReviewEvent(review.getUserId(), review.getReviewId());
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
