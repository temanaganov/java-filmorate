package ru.yandex.practicum.filmorate.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.review.ReviewDto;
import ru.yandex.practicum.filmorate.model.review.Review;

@Component
public class ReviewDtoToReviewMapper implements Mapper<ReviewDto, Review> {
    @Override
    public Review mapFrom(ReviewDto dto) {
        return new Review(
                dto.getReviewId(),
                dto.getContent(),
                dto.getIsPositive(),
                dto.getUserId(),
                dto.getFilmId(),
                0
        );
    }
}
