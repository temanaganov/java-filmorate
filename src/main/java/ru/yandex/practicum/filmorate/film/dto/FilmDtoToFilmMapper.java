package ru.yandex.practicum.filmorate.film.dto;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.core.util.Mapper;
import ru.yandex.practicum.filmorate.film.model.Film;

import java.util.Collections;
import java.util.Optional;

@Component
public class FilmDtoToFilmMapper implements Mapper<FilmDto, Film> {
    @Override
    public Film mapFrom(FilmDto dto) {
        return new Film(
                dto.getId(),
                dto.getName(),
                dto.getDescription(),
                dto.getReleaseDate(),
                dto.getDuration(),
                dto.getMpa(),
                Optional.ofNullable(dto.getGenres()).orElse(Collections.emptyList()),
                Optional.ofNullable(dto.getDirectors()).orElse(Collections.emptyList())
        );
    }
}
