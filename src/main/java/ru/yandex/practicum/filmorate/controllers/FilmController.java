package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exceptions.UserDoesNotExist;
import ru.yandex.practicum.filmorate.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;
@Slf4j
@RestController
@ResponseBody
@RequiredArgsConstructor
public class FilmController {
    private final FilmStorage filmStorage;

    @GetMapping("/films")
    public Collection<Film> findAllFilms() {
        log.debug("Получен запрос GET. Количество фильмов {}:", filmStorage.findAllFilms().size());
        return filmStorage.findAllFilms();
    }

    @GetMapping("films/popular")
    public Collection<Film> findPopularFilm(@RequestParam(defaultValue = "10") Integer count) {

        log.debug("Получен запрос GET. Количество популярных фильмов {}:", filmStorage.findPopularFilm(count).size());
        return filmStorage.findPopularFilm(count);
    }

    @GetMapping("/films/{filmId}")
    public Film findFilmById(@PathVariable Integer filmId) {
        log.debug("Получен запрос GET:" + filmStorage.findFilm(filmId));
        return filmStorage.findFilm(filmId);
    }

    @PostMapping(value = "/films")
    public Film addFilm(@RequestBody Film film) {
        log.debug("Получен запрос POST: " + film);
        filmStorage.addFilm(film);
        return film;
    }

    @PutMapping("/films")
    public Film update(@RequestBody Film film) {
        log.debug("Получен запрос PUT: " + film);
        return filmStorage.updateFilm(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film likeToFilm(@PathVariable int id,@PathVariable int userId) {
        log.debug("Получен запрос PUT. Ставим лайк фильму");
        return filmStorage.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film removeLike(@PathVariable int id, @PathVariable int userId) throws UserDoesNotExist {
        log.debug("Получен запрос PUT. Удаляем лайк фильму");
        return filmStorage.removeLike(id, userId);
    }

}
