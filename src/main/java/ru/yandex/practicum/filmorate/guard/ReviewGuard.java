package ru.yandex.practicum.filmorate.guard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;

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
