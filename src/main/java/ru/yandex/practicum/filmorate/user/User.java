package ru.yandex.practicum.filmorate.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@With
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    int id;

    @Email
    String email;

    @NotBlank
    String login;

    String name;

    @PastOrPresent
    LocalDate birthday;
}
