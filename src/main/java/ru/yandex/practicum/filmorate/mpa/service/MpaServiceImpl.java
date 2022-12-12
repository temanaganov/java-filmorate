package ru.yandex.practicum.filmorate.mpa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.core.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mpa.model.Mpa;
import ru.yandex.practicum.filmorate.mpa.storage.MpaStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaServiceImpl implements MpaService {
    private final MpaStorage mpaStorage;

    @Override
    public List<Mpa> getAll() {
        return mpaStorage.getAll();
    }

    @Override
    public Mpa getById(int id) {
        Mpa mpa = mpaStorage.getById(id);

        if (mpa == null) {
            throw new NotFoundException("mpa", id);
        }

        return mpa;
    }
}
