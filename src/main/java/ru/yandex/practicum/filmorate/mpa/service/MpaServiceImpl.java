package ru.yandex.practicum.filmorate.mpa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.mpa.model.Mpa;
import ru.yandex.practicum.filmorate.mpa.storage.MpaStorage;
import ru.yandex.practicum.filmorate.mpa.util.MpaGuard;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaServiceImpl implements MpaService {
    private final MpaStorage mpaStorage;
    private final MpaGuard mpaGuard;

    @Override
    public List<Mpa> getAll() {
        return mpaStorage.getAll();
    }

    @Override
    public Mpa getById(int id) {
        return mpaGuard.checkIfExists(id);
    }
}
