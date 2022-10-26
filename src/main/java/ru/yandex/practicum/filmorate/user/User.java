package ru.yandex.practicum.filmorate.user;

import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Value
public class User {
    int id;

    @Email
    String email;

    @NotBlank
    String login;

    String name;

    @DateTimeFormat(pattern = "YYYY-mm-dd")
    @PastOrPresent
    LocalDate birthday;
}
