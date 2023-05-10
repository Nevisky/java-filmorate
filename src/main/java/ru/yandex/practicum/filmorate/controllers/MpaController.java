package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.interfaces.MpaStorage;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MpaController {

    private final MpaStorage mpaStorage;


    @GetMapping("/mpa")
    @ResponseBody
    public Collection<Mpa> getMpaList() {
        return mpaStorage.getMpaList();
    }

    @GetMapping("/mpa/{id}")
    @ResponseBody
    public Mpa getMpaById(@PathVariable("id") Integer ratingId) {
        return mpaStorage.getMpaById(ratingId);
    }
}