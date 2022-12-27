package ru.yandex.practicum.filmorate.service.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.mapper.Mapper;
import ru.yandex.practicum.filmorate.service.event.EventService;
import ru.yandex.practicum.filmorate.service.event.EventServiceImpl;
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
