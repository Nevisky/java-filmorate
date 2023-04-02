package ru.yandex.practicum.filmorate.eceptions;

public class FilmReleaseDateCouldNotBeEarlyThanCertainDate extends RuntimeException {
    public FilmReleaseDateCouldNotBeEarlyThanCertainDate(String msg) {
        super(msg);
    }
}
