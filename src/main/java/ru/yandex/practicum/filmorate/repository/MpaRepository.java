package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaRepository {
    List<Mpa> getAll();
    Mpa getById(int id);
}
