package ru.yandex.practicum.filmorate.eceptions;

public class FilmAlreadyExistException extends RuntimeException {
    public FilmAlreadyExistException(String msg) {
        super(msg);
    }
}
