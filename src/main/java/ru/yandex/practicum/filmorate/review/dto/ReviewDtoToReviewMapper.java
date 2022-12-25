package ru.yandex.practicum.filmorate.review.dto;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.core.util.Mapper;
import ru.yandex.practicum.filmorate.review.model.Review;

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
