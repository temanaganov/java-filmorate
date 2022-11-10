package ru.yandex.practicum.filmorate.film;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.core.exception.ExceptionsHandler;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class FilmControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private MockMvc mockMvc;

    @Mock
    private FilmService filmService;

    @InjectMocks
    private FilmController filmController;

    @BeforeEach
    public void setMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(filmController).setControllerAdvice(new ExceptionsHandler()).build();
    }

    @Test
    public void getAll_shouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void getAll_shouldReturnFilms() throws Exception {
        List<Film> films = List.of(getFilm(1), getFilm(2));
        when(filmService.getAll()).thenReturn(films);

        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(films)));
    }

    @Test
    public void create_shouldSuccessfullyCreateAndUpdateFilm() throws Exception {
        Film film = getFilm(1);
        String json = objectMapper.writeValueAsString(film);

        mockMvc.perform(post("/films")
                        .accept("application/json")
                        .contentType("application/json")
                        .content(json)
        ).andExpect(status().isCreated());

        json = objectMapper.writeValueAsString(film.withName("Updated name"));

        mockMvc.perform(put("/films")
                .accept("application/json")
                .contentType("application/json")
                .content(json)
        ).andExpect(status().isOk());
    }


    @Test
    public void create_shouldResponseWithBadRequest_ifFilmIsInvalid() throws Exception {
        Film film;
        String json;

        film = getFilm(1).withName("");
        json = objectMapper.writeValueAsString(film);
        mockMvc.perform(post("/films").accept("application/json").contentType("application/json").content(json))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/films").contentType("application/json").content(json))
                .andExpect(status().isBadRequest());

        film = getFilm(1).withDescription("a long string".repeat(20));
        json = objectMapper.writeValueAsString(film);
        mockMvc.perform(post("/films").contentType("application/json").content(json))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/films").contentType("application/json").content(json))
                .andExpect(status().isBadRequest());

        film = getFilm(1).withReleaseDate(LocalDate.parse("1000-01-01"));
        json = objectMapper.writeValueAsString(film);
        mockMvc.perform(post("/films").contentType("application/json").content(json))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/films").contentType("application/json").content(json))
                .andExpect(status().isBadRequest());

        film = getFilm(1).withDuration(-1);
        json = objectMapper.writeValueAsString(film);
        mockMvc.perform(post("/films").contentType("application/json").content(json))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/films").contentType("application/json").content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void delete_shouldSuccessfullyDeleteFilm() throws Exception {
        when(filmService.delete(1)).thenReturn(getFilm(1));

        mockMvc.perform(delete("/films/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(getFilm(1))));
    }

    @Test
    public void shouldSuccessfullyAddAndRemoveLikeFromFilm() throws Exception {
        Film film = getFilm(1);
        Film filmWithLikes = film.withLikes(Set.of(1));

        when(filmService.likeFilm(1, 1)).thenReturn(filmWithLikes);
        when(filmService.deleteLikeFromFilm(1, 1)).thenReturn(film);

        mockMvc.perform(put("/films/1/like/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(filmWithLikes)));

        mockMvc.perform(delete("/films/1/like/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(film)));
    }

    @Test
    public void shouldReturnFilmsByLikes() throws Exception {
        Film film1 = getFilm(1).withLikes(Set.of(1));
        Film film2 = getFilm(2).withLikes(Set.of(1, 2));
        Film film3 = getFilm(3).withLikes(Set.of(1, 2, 3));

        List<Film> films = List.of(film3, film2, film1);

        when(filmService.getPopularFilms(10)).thenReturn(films);

        mockMvc.perform(get("/films/popular?count=10"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(films)));
    }

    private static Film getFilm(int id) {
        return new Film(id, "film", "description", LocalDate.parse("2000-01-01"), 120, Collections.emptySet());
    }
}
