package ru.yandex.practicum.filmorate.mpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.mpa.model.Mpa;
import ru.yandex.practicum.filmorate.mpa.service.MpaService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    @GetMapping
    public List<Mpa> getAllMpas() {
        return mpaService.getAll();
    }

    @GetMapping("/{id}")
    public Mpa getMpa(@PathVariable int id) {
        return mpaService.getById(id);
    }
}
