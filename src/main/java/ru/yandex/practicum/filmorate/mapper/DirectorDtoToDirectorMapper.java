package ru.yandex.practicum.filmorate.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.director.Director;
import ru.yandex.practicum.filmorate.model.director.DirectorDto;

@Component
public class DirectorDtoToDirectorMapper implements Mapper<DirectorDto, Director> {
    @Override
    public Director mapFrom(DirectorDto dto) {
        return new Director(dto.getId(), dto.getName());
    }
}
