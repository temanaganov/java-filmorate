package ru.yandex.practicum.filmorate.mpa.storage;

import ru.yandex.practicum.filmorate.mpa.model.Mpa;

import java.util.List;

public interface MpaStorage {
    List<Mpa> getAll();
    Mpa getById(int id);
}
