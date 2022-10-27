package ru.yandex.practicum.filmorate.film;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.Value;
import lombok.With;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;

@With
@Value
public class Film {
    int id;

    @NotBlank
    String name;

    @NotBlank
    @Size(max = 200)
    String description;

    @DateTimeFormat(pattern = "YYYY-mm-dd")
    LocalDate releaseDate;

    @Positive
    int duration;
}
