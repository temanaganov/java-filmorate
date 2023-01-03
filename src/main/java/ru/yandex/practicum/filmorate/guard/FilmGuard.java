package ru.yandex.practicum.filmorate.guard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

@Component
@RequiredArgsConstructor
public class FilmGuard extends Guard<Film> {
    private final FilmStorage filmStorage;

    @Override
    protected String getGuardClass() {
        return "Film";
    }

    @Override
    protected Film checkMethod(int id) {
        return filmStorage.getById(id);
    }
}
