package ru.yandex.practicum.filmorate.eceptions;

public class FilmDoesNotExist extends RuntimeException {
    public FilmDoesNotExist(String msg) {
        super(msg);
    }
}
