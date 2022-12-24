package ru.yandex.practicum.filmorate.director.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.core.util.Guard;
import ru.yandex.practicum.filmorate.core.util.Mapper;
import ru.yandex.practicum.filmorate.director.dto.DirectorDto;
import ru.yandex.practicum.filmorate.director.model.Director;
import ru.yandex.practicum.filmorate.director.storage.DirectorStorage;

import java.util.List;

@Service
public class DirectorServiceImpl implements DirectorService {
    private final DirectorStorage directorStorage;
    private final Mapper<DirectorDto, Director> directorDtoToDirectorMapper;
    private final Guard<Director> directorGuard;

    public DirectorServiceImpl(
            DirectorStorage directorStorage,
            Mapper<DirectorDto, Director> directorDtoToDirectorMapper
    ) {
        this.directorStorage = directorStorage;
        this.directorDtoToDirectorMapper = directorDtoToDirectorMapper;
        this.directorGuard  = new Guard<>(directorStorage::getById, Director.class);
    }

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
