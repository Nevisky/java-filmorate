package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.eceptions.UserDoesNotExist;
import ru.yandex.practicum.filmorate.managers.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import java.util.Collection;
@Slf4j
@RestController
@ResponseBody
public class FilmController {
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }


    @GetMapping("/films")
    public Collection<Film> findAllFilms() {
        log.debug("Получен запрос GET. Количество фильмов {}:", inMemoryFilmStorage.findAllFilms().size());
        return inMemoryFilmStorage.findAllFilms();
    }

    @GetMapping("films/popular")
    public Collection<Film> findPopularFilm(@RequestParam(defaultValue = "10",required = false) Integer count) {
        log.debug("Получен запрос GET. Количество популярных фильмов {}:", inMemoryFilmStorage.findAllFilms().size());
        return filmService.findPopularFilm(count);
    }

    @GetMapping("/films/{filmId}")
    public Film findFilmById(@PathVariable Integer filmId) {
        log.debug("Получен запрос GET:" + inMemoryFilmStorage.getFilms().get(filmId));
        return inMemoryFilmStorage.findFilm(filmId);
    }

    @PostMapping(value = "/films")
    public Film addFilm(@RequestBody Film film) {
        log.debug("Получен запрос POST: " + film);
        inMemoryFilmStorage.addFilm(film);
        return film;
    }

    @PutMapping("/films")
    public Film update(@RequestBody Film film) {
        log.debug("Получен запрос PUT: " + film);
        return inMemoryFilmStorage.updateFilm(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film likeToFilm(@PathVariable int id,@PathVariable int userId) {
        log.debug("Получен запрос PUT. Ставим лайк фильму");
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film removeLike(@PathVariable int id, @PathVariable int userId) throws UserDoesNotExist {
        log.debug("Получен запрос PUT. Удаляем лайк фильму");
        return filmService.removeLike(id, userId);
    }

}
