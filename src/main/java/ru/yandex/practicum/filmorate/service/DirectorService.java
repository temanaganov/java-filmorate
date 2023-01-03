package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorService {
    List<Director> getAll();
    Director getById(int id);
    Director create(DirectorDto dto);
    Director update(DirectorDto dto);
    Director delete(int id);
}
