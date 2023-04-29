package ru.yandex.practicum.filmorate.exceptions;

public class FilmDurationsMustBePositive extends RuntimeException {
    public FilmDurationsMustBePositive(String msg) {
        super(msg);
    }
}
