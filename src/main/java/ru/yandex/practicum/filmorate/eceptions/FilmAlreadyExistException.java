package ru.yandex.practicum.filmorate.eceptions;

public class FilmAlreadyExistException extends Throwable {
    public FilmAlreadyExistException(String msg) {
        super(msg);
    }
}
