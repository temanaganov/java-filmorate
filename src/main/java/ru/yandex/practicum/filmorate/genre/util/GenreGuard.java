package ru.yandex.practicum.filmorate.genre.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.core.util.Guard;
import ru.yandex.practicum.filmorate.genre.model.Genre;
import ru.yandex.practicum.filmorate.genre.storage.GenreStorage;

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
