package ru.yandex.practicum.filmorate.guard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.repository.ReviewRepository;

@Component
@RequiredArgsConstructor
public class ReviewGuard extends Guard<Review> {
    private final ReviewRepository reviewRepository;

    @Override
    protected String getGuardClass() {
        return "Review";
    }

    @Override
    protected Review checkMethod(int id) {
        return reviewRepository.getById(id);
    }
}
