package ru.yandex.practicum.filmorate.genre.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.core.exception.NotFoundException;
import ru.yandex.practicum.filmorate.genre.model.Genre;
import ru.yandex.practicum.filmorate.genre.storage.GenreStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreStorage genreStorage;

    @Override
    public List<Genre> getAll() {
        return genreStorage.getAll();
    }

    @Override
    public Genre getById(int id) {
        Genre genre = genreStorage.getById(id);

        if (genre == null) {
            throw new NotFoundException("genre", id);
        }

        return genre;
    }
}
