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

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
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
    void setMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(filmController).build();
    }

    @Test
    void getAll_shouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getAll_shouldReturnFilms() throws Exception {
        List<Film> films = List.of(getFilm(), getFilm());
        when(filmService.getAllFilms()).thenReturn(films);

        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(films)));
    }

    @Test
    void create_shouldResponseWithBadRequest_ifFilmIsInvalid() throws Exception {
        Film film;
        String json;

        film = getFilm().withName("");
        json = objectMapper.writeValueAsString(film);
        mockMvc.perform(post("/films").accept("application/json").contentType("application/json").content(json))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/films").contentType("application/json").content(json))
                .andExpect(status().isBadRequest());

        film = getFilm().withDescription("a long string".repeat(20));
        json = objectMapper.writeValueAsString(film);
        mockMvc.perform(post("/films").contentType("application/json").content(json))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/films").contentType("application/json").content(json))
                .andExpect(status().isBadRequest());

        film = getFilm().withReleaseDate(LocalDate.parse("1000-01-01"));
        json = objectMapper.writeValueAsString(film);
        mockMvc.perform(post("/films").contentType("application/json").content(json))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/films").contentType("application/json").content(json))
                .andExpect(status().isBadRequest());

        film = getFilm().withDuration(-1);
        json = objectMapper.writeValueAsString(film);
        mockMvc.perform(post("/films").contentType("application/json").content(json))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/films").contentType("application/json").content(json))
                .andExpect(status().isBadRequest());
    }

    private static Film getFilm() {
        return new Film(1, "film", "description", LocalDate.parse("2000-01-01"), 120);
    }
}
