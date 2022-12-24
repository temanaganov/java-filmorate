package ru.yandex.practicum.filmorate.genre.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.core.util.Guard;
import ru.yandex.practicum.filmorate.genre.model.Genre;
import ru.yandex.practicum.filmorate.genre.storage.GenreStorage;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreStorage genreStorage;
    private final Guard<Genre> genreGuard;

    public GenreServiceImpl(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
        this.genreGuard = new Guard<>(genreStorage::getById, Genre.class);
    }

    @Override
    public List<Genre> getAll() {
        return genreStorage.getAll();
    }

    @Override
    public Genre getById(int id) {
        return genreGuard.checkIfExists(id);
    }
}
