package ru.yandex.practicum.filmorate.director.service;

import ru.yandex.practicum.filmorate.director.dto.DirectorDto;
import ru.yandex.practicum.filmorate.director.model.Director;

import java.util.List;

public interface DirectorService {
    List<Director> getAll();
    Director getById(int id);
    Director create(DirectorDto dto);
    Director update(DirectorDto dto);
    Director delete(int id);
}
