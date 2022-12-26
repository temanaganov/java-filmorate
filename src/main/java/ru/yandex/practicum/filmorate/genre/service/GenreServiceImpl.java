package ru.yandex.practicum.filmorate.genre.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.genre.model.Genre;
import ru.yandex.practicum.filmorate.genre.storage.GenreStorage;
import ru.yandex.practicum.filmorate.genre.util.GenreGuard;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreStorage genreStorage;
    private final GenreGuard genreGuard;

    @Override
    public List<Genre> getAll() {
        return genreStorage.getAll();
    }

    @Override
    public Genre getById(int id) {
        return genreGuard.checkIfExists(id);
    }
}
