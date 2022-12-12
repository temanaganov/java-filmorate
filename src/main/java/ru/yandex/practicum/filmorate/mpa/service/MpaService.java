package ru.yandex.practicum.filmorate.mpa.service;

import ru.yandex.practicum.filmorate.mpa.model.Mpa;

import java.util.List;

public interface MpaService {
    List<Mpa> getAll();
    Mpa getById(int id);
}
