package ru.yandex.practicum.filmorate.eceptions;

public class FilmDurationsMustBePositive extends RuntimeException {
    public FilmDurationsMustBePositive(String msg) {
        super(msg);
    }
}
