package ru.yandex.practicum.filmorate.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
public interface FilmStorage {

    Collection<Film> findAllFilms();

    void addFilm(Film film);
    Film findFilm(int filmId);
    Film updateFilm(Film film);


}
