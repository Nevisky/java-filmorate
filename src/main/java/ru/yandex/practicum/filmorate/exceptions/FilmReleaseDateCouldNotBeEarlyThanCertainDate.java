package ru.yandex.practicum.filmorate.exceptions;

public class FilmReleaseDateCouldNotBeEarlyThanCertainDate extends RuntimeException {
    public FilmReleaseDateCouldNotBeEarlyThanCertainDate(String msg) {
        super(msg);
    }
}
