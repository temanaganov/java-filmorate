package ru.yandex.practicum.filmorate.service.director;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.mapper.Mapper;
import ru.yandex.practicum.filmorate.model.director.DirectorDto;
import ru.yandex.practicum.filmorate.model.director.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;
import ru.yandex.practicum.filmorate.guard.DirectorGuard;

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
