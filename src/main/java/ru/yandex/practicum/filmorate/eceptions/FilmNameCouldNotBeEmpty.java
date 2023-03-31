package ru.yandex.practicum.filmorate.eceptions;

public class FilmNameCouldNotBeEmpty extends Throwable {
    public FilmNameCouldNotBeEmpty(String msg) {
        super(msg);
    }
}
