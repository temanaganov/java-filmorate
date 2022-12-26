package ru.yandex.practicum.filmorate.review.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.core.util.Guard;
import ru.yandex.practicum.filmorate.review.model.Review;
import ru.yandex.practicum.filmorate.review.storage.ReviewStorage;

@Component
@RequiredArgsConstructor
public class ReviewGuard extends Guard<Review> {
    private final ReviewStorage reviewStorage;

    @Override
    protected String getGuardClass() {
        return "Review";
    }

    @Override
    protected Review checkMethod(int id) {
        return reviewStorage.getById(id);
    }
}
