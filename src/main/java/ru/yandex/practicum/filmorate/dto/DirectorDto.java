package ru.yandex.practicum.filmorate.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

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
