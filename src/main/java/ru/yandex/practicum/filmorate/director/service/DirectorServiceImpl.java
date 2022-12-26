package ru.yandex.practicum.filmorate.director.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.core.util.Mapper;
import ru.yandex.practicum.filmorate.director.dto.DirectorDto;
import ru.yandex.practicum.filmorate.director.model.Director;
import ru.yandex.practicum.filmorate.director.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.director.util.DirectorGuard;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectorServiceImpl implements DirectorService {
    private final DirectorStorage directorStorage;
    private final Mapper<DirectorDto, Director> directorDtoToDirectorMapper;
    private final DirectorGuard directorGuard;

    @Override
    public List<Director> getAll() {
        return directorStorage.getAll();
    }

    @Override
    public Director getById(int id) {
        return directorGuard.checkIfExists(id);
    }

    @Override
    public Director create(DirectorDto dto) {
        Director director = directorDtoToDirectorMapper.mapFrom(dto);

        return directorStorage.create(director);
    }

    @Override
    public Director update(DirectorDto dto) {
        directorGuard.checkIfExists(dto.getId());
        Director director = directorDtoToDirectorMapper.mapFrom(dto);

        return directorStorage.update(director);
    }

    @Override
    public Director delete(int id) {
        Director director = directorGuard.checkIfExists(id);
        directorStorage.delete(id);

        return director;
    }
}
