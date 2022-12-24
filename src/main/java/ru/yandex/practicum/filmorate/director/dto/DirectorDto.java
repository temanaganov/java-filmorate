package ru.yandex.practicum.filmorate.director.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@With
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class DirectorDto {
    int id;

    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name must be maximum 50 characters")
    String name;
}
