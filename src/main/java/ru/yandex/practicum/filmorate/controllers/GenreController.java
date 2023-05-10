package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.interfaces.GenreStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;


@RestController
@Slf4j
@RequiredArgsConstructor
public class GenreController {

    private final GenreStorage genreStorage;

    @GetMapping("/genres")
    public Collection<Genre> getGenresList() {
        return genreStorage.getGenresList();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenreById(@PathVariable("id") Integer genreId) {
        return genreStorage.getGenreById(genreId);
    }
}