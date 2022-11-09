package ru.yandex.practicum.filmorate.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.core.exception.FieldValidationException;
import ru.yandex.practicum.filmorate.film.dto.FilmDto;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        return filmService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@Valid @RequestBody FilmDto dto) {
        log.info("Creating Film " + dto);
        validateFilmReleaseDate(dto.getReleaseDate());
        return filmService.create(dto);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody FilmDto dto) {
        log.info("Updating Film " + dto);
        validateFilmReleaseDate(dto.getReleaseDate());
        return filmService.update(dto);
    }

    @DeleteMapping("/{id}")
    public Film deleteFilm(@PathVariable int id) {
        log.info("Deleting Film " + id);
        return filmService.delete(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopularFilms(count);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film likeFilm(@PathVariable int id, @PathVariable int userId) {
        return filmService.likeFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLikeFromFilm(@PathVariable int id, @PathVariable int userId) {
        return filmService.deleteLikeFromFilm(id, userId);
    }

    private void validateFilmReleaseDate(LocalDate date) {
        if (date.isBefore(LocalDate.of(1895, 12, 28))) {
            throw new FieldValidationException("releaseDate", "Release date must not be before 1895-12-28");
        }
    }
}
