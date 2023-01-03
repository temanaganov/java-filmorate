package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.repository.MpaRepository;
import ru.yandex.practicum.filmorate.guard.MpaGuard;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaServiceImpl implements MpaService {
    private final MpaRepository mpaRepository;
    private final MpaGuard mpaGuard;

    @Override
    public List<Mpa> getAll() {
        return mpaRepository.getAll();
    }

    @Override
    public Mpa getById(int id) {
        return mpaGuard.checkIfExists(id);
    }
}
