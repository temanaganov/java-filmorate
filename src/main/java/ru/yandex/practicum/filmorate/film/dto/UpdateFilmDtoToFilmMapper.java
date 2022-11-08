package ru.yandex.practicum.filmorate.film.dto;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.core.util.Mapper;
import ru.yandex.practicum.filmorate.film.Film;

@Component
public class UpdateFilmDtoToFilmMapper implements Mapper<UpdateFilmDto, Film> {
    @Override
    public Film mapFrom(UpdateFilmDto dto) {
        Film.FilmBuilder filmBuilder = Film.builder();
        filmBuilder.id(dto.getId());
        filmBuilder.name(dto.getName());
        filmBuilder.description(dto.getDescription());
        filmBuilder.releaseDate(dto.getReleaseDate());
        filmBuilder.duration(dto.getDuration());

        return filmBuilder.build();
    }
}
