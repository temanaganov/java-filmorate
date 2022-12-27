package ru.yandex.practicum.filmorate.guard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.genre.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

@Component
@RequiredArgsConstructor
public class GenreGuard extends Guard<Genre> {
    private final GenreStorage genreStorage;

    @Override
    protected String getGuardClass() {
        return "Genre";
    }

    @Override
    protected Genre checkMethod(int id) {
        return genreStorage.getById(id);
    }
}
