package ru.yandex.practicum.filmorate.guard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;

@Component
@RequiredArgsConstructor
public class GenreGuard extends Guard<Genre> {
    private final GenreRepository genreRepository;

    @Override
    protected String getGuardClass() {
        return "Genre";
    }

    @Override
    protected Genre checkMethod(int id) {
        return genreRepository.getById(id);
    }
}
