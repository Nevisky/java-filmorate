package ru.yandex.practicum.filmorate.eceptions;

public class FilmNameCouldNotBeEmpty extends RuntimeException {
    public FilmNameCouldNotBeEmpty(String msg) {
        super(msg);
    }
}
