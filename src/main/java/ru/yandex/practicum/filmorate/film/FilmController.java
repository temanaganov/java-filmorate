package ru.yandex.practicum.filmorate.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService = new InMemoryFilmService();

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable("id") int id) {
        Film film = filmService.getFilm(id);

        if (film == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return film;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@Valid @RequestBody Film film) {
        validateFilmReleaseDate(film.getReleaseDate());
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        validateFilmReleaseDate(film.getReleaseDate());
        Film updatedFilm = filmService.updateFilm(film.getId(), film);

        if (updatedFilm == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return updatedFilm;
    }

    @DeleteMapping("/{id}")
    public Film deleteFilm(@PathVariable("id") int id) {
        Film deleteFilm = filmService.deleteFilm(id);

        if (deleteFilm == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return deleteFilm;
    }

    private void validateFilmReleaseDate(LocalDate date) {
        if (date.isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
