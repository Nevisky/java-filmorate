package ru.yandex.practicum.filmorate.eceptions;

public class FilmDurationsMustBePositive extends Throwable {
    public FilmDurationsMustBePositive(String msg) {
        super(msg);
    }
}
