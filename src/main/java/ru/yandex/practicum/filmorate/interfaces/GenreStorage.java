package ru.yandex.practicum.filmorate.interfaces;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface GenreStorage {

    Genre getGenreById(Integer genreId);

    Collection<Genre> getGenresList();
}