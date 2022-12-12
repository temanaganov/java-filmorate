package ru.yandex.practicum.filmorate.core.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.film.service.FilmService;
import ru.yandex.practicum.filmorate.film.dto.FilmDto;
import ru.yandex.practicum.filmorate.mpa.model.Mpa;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class ExceptionsHandlerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilmService filmService;

    @Test
    void fieldExceptionHandler_shouldReturnListOfFieldErrors() throws Exception {
        FilmDto filmDto = getFilmDto(1).withName("").withDuration(-1);
        String json = objectMapper.writeValueAsString(filmDto);

        List<FieldError> errors = List.of(
                new FieldError("name", "Name is required"),
                new FieldError("duration", "Duration is required and must be greater than 0")
        );

        mockMvc.perform(post("/films").contentType("application/json").content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(errors)));

        filmDto = getFilmDto(1).withReleaseDate(LocalDate.MIN);
        json = objectMapper.writeValueAsString(filmDto);

        errors = List.of(new FieldError("releaseDate", "Release date must not be before 1895-12-28"));

        mockMvc.perform(post("/films").contentType("application/json").content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(errors)));
    }

    @Test
    void notFoundException_shouldReturnNotFoundError() throws Exception {
        int id = 1;

        Map<String, String> error = Map.of("error", "film with id=" + id + " not found");

        when(filmService.getById(id)).thenThrow(new NotFoundException("film", id));

        mockMvc.perform(get("/films/" + id).contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(error)));
    }

    @Test
    void internalServerErrorHandler_shouldReturnNotFoundError() throws Exception {
        String message = "test message";
        Map<String, String> error = Map.of("error", message);

        when(filmService.getAll()).thenThrow(new RuntimeException(message));

        mockMvc.perform(get("/films").contentType("application/json"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json(objectMapper.writeValueAsString(error)));
    }

    private static FilmDto getFilmDto(int id) {
        return new FilmDto(
                id,
                "Test film",
                "Test description",
                LocalDate.parse("2000-01-01"),
                120,
                new Mpa(1, "G"),
                Collections.emptyList()
        );
    }
}
