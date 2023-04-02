package ru.yandex.practicum.filmorate.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.eceptions.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
@Slf4j
@RestController
@ResponseBody
public class FilmController {
    private int id;
    private HashSet<Film> films = new HashSet<>();

    @GetMapping("/films")
    public Collection<Film> findAllFilms() {
        log.debug("Получен запрос POST. Количество фильмов {}:",films.size());
        return new ArrayList<>(films);
    }

    @PostMapping(value = "/films")
    public Film addFilm(@NotNull @Valid @RequestBody Film film) {
        log.debug("Получен запрос POST: " + film);
        if (film.getName().isBlank()) {
            throw new FilmNameCouldNotBeEmpty("Наименование фильма не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new FilmDescriptionCouldNotBeMore200Symbols("Описание фильма превышает 200 символов");
        }
        if (film.getReleaseDate().isBefore(ChronoLocalDate.from(LocalDate.of(1895, 12, 28)))) {
            throw new FilmReleaseDateCouldNotBeEarlyThanCertainDate("Данный фильм выпущен раньше, чем 28.12.1985 года");
        }
        if (film.getDuration() < 0) {
            throw new FilmDurationsMustBePositive("Продолжительность фильма не может быть отрицательной");
        }
        if (films.contains(film)) {
            throw new FilmAlreadyExistException("Фильм уже был добавлен ранее");
        }
        film.setId(++id);
        films.add(film);
        return film;
    }


    @PutMapping("/films")
    public Film update(@NotNull @Valid @RequestBody Film film) {
        log.debug("Получен запрос PUT: " + film) ;

        if (film == null) {
            throw new InvalidEmailException("Некорректный film");
        } else {
            for (Film findFilm : films) {
                if (findFilm.getId() == film.getId()) {
                    findFilm.setId(film.getId());
                    findFilm.setName(film.getName());
                    findFilm.setDescription(film.getDescription());
                    findFilm.setReleaseDate(film.getReleaseDate());
                    findFilm.setDuration(film.getDuration());
                } else {
                    throw new FilmDoesNotExist("Данного фильма не существует, невозможно обновить данные");
                }
            }

        }

        return film;
    }
}
