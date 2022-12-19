package ru.yandex.practicum.filmorate.director.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.core.exception.NotFoundException;
import ru.yandex.practicum.filmorate.core.util.Mapper;
import ru.yandex.practicum.filmorate.director.dto.DirectorDto;
import ru.yandex.practicum.filmorate.director.model.Director;
import ru.yandex.practicum.filmorate.director.storage.DirectorStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectorServiceImpl implements DirectorService {
    private final DirectorStorage directorStorage;
    private final Mapper<DirectorDto, Director> directorDtoToDirectorMapper;

    @Override
    public List<Director> getAll() {
        return directorStorage.getAll();
    }

    @Override
    public Director getById(int id) {
        Director director = directorStorage.getById(id);

        if (director == null) {
            throw new NotFoundException("director", id);
        }

        return director;
    }

    @Override
    public Director create(DirectorDto dto) {
        Director director = directorDtoToDirectorMapper.mapFrom(dto);
        return directorStorage.create(director);
    }

    @Override
    public Director update(DirectorDto dto) {
        Director currentDirector = directorStorage.getById(dto.getId());

        if (currentDirector == null){
            throw new NotFoundException("director", dto.getId());
        }

        Director director = directorDtoToDirectorMapper.mapFrom(dto);

        return directorStorage.update(director);
    }

    @Override
    public Director delete(int id) {
        Director director = directorStorage.delete(id);

        if (director == null) {
            throw new NotFoundException("director", id);
        }

        return director;
    }
}
