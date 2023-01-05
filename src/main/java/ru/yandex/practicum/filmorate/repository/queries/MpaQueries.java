package ru.yandex.practicum.filmorate.repository.queries;

public class MpaQueries {
    public static final String GET_ALL = "SELECT * FROM mpa ORDER BY mpa_id";

    public static final String GET_BY_ID = "SELECT * FROM mpa WHERE mpa_id = ?";
}
