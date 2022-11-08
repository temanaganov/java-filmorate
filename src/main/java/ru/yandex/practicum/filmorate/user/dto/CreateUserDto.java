package ru.yandex.practicum.filmorate.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@With
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto {
    @Email(message = "Email is incorrect")
    @NotNull(message = "Email is required")
    String email;

    @NotNull(message = "Login is required")
    @Pattern(regexp = "\\S+", message = "Login must not contain space characters")
    String login;

    String name;

    @NotNull(message = "Birthday is required")
    @PastOrPresent(message = "Birthday must not be later than the current date")
    LocalDate birthday;
}
