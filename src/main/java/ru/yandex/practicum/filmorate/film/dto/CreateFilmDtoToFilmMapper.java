package ru.yandex.practicum.filmorate.film.dto;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.core.util.Mapper;
import ru.yandex.practicum.filmorate.film.Film;

import java.util.HashSet;

@Component
public class CreateFilmDtoToFilmMapper implements Mapper<CreateFilmDto, Film> {
    @Override
    public Film mapFrom(CreateFilmDto dto) {
        Film.FilmBuilder filmBuilder = Film.builder();
        filmBuilder.name(dto.getName());
        filmBuilder.description(dto.getDescription());
        filmBuilder.releaseDate(dto.getReleaseDate());
        filmBuilder.duration(dto.getDuration());
        filmBuilder.likes(new HashSet<>());

        return filmBuilder.build();
    }
}
