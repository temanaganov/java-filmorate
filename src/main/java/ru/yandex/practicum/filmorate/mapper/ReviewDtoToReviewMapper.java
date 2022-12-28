package ru.yandex.practicum.filmorate.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.review.ReviewDto;
import ru.yandex.practicum.filmorate.model.review.Review;

@Component
public class ReviewDtoToReviewMapper implements Mapper<ReviewDto, Review> {
    @Override
    public Review mapFrom(ReviewDto dto) {
        return Review.builder()
                .reviewId(dto.getReviewId())
                .content(dto.getContent())
                .isPositive(dto.getIsPositive())
                .userId(dto.getUserId())
                .filmId(dto.getFilmId())
                .useful(0)
                .build();
    }
}
