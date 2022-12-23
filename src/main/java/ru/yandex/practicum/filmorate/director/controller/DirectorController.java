package ru.yandex.practicum.filmorate.director.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.director.dto.DirectorDto;
import ru.yandex.practicum.filmorate.director.model.Director;
import ru.yandex.practicum.filmorate.director.service.DirectorService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/directors")
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping
    public List<Director> getAllDirectors() {
        return directorService.getAll();
    }

    @GetMapping("/{id}")
    public Director getDirector(@PathVariable int id) {
        return directorService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Director createDirector(@Valid @RequestBody DirectorDto dto) {
        return directorService.create(dto);
    }

    @PutMapping
    public Director updateDirector(@Valid @RequestBody DirectorDto dto) {
        return directorService.update(dto);
    }

    @DeleteMapping("/{id}")
    public Director deleteDirector(@PathVariable int id) {
        return directorService.delete(id);
    }
}
