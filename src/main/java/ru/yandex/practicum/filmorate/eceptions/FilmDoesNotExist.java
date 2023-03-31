package ru.yandex.practicum.filmorate.eceptions;

public class FilmDoesNotExist extends Throwable {
    public FilmDoesNotExist(String msg) {
        super(msg);
    }
}
