package ru.yandex.practicum.filmorate.eceptions;

public class FilmReleaseDateCouldNotBeEarlyThanCertainDate extends Throwable {
    public FilmReleaseDateCouldNotBeEarlyThanCertainDate(String msg) {
        super(msg);
    }
}
