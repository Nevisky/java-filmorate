package ru.yandex.practicum.filmorate.exceptions;

public class FilmDoesNotExist extends RuntimeException {
    public FilmDoesNotExist(String msg) {
        super(msg);
    }
}
