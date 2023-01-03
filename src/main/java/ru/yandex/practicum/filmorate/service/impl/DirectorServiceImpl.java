package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.mapper.Mapper;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.repository.DirectorRepository;
import ru.yandex.practicum.filmorate.guard.DirectorGuard;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectorServiceImpl implements DirectorService {
    private final DirectorRepository directorRepository;
    private final Mapper<DirectorDto, Director> directorDtoToDirectorMapper;
    private final DirectorGuard directorGuard;

    @Override
    public List<Director> getAll() {
        return directorRepository.getAll();
    }

    @Override
    public Director getById(int id) {
        return directorGuard.checkIfExists(id);
    }

    @Override
    public Director create(DirectorDto dto) {
        Director director = directorDtoToDirectorMapper.mapFrom(dto);

        return directorRepository.create(director);
    }

    @Override
    public Director update(DirectorDto dto) {
        directorGuard.checkIfExists(dto.getId());
        Director director = directorDtoToDirectorMapper.mapFrom(dto);

        return directorRepository.update(director);
    }

    @Override
    public Director delete(int id) {
        Director director = directorGuard.checkIfExists(id);
        directorRepository.delete(id);

        return director;
    }
}
