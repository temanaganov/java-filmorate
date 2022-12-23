package ru.yandex.practicum.filmorate.director.dto;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.core.util.Mapper;
import ru.yandex.practicum.filmorate.director.model.Director;

@Component
public class DirectorDtoToDirectorMapper implements Mapper<DirectorDto, Director> {
    @Override
    public Director mapFrom(DirectorDto dto) {
        return new Director(dto.getId(), dto.getName());
    }
}
