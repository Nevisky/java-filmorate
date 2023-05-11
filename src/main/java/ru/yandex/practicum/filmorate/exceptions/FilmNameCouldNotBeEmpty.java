package ru.yandex.practicum.filmorate.exceptions;

public class FilmNameCouldNotBeEmpty extends RuntimeException {
    public FilmNameCouldNotBeEmpty(String msg) {
        super(msg);
    }
}
