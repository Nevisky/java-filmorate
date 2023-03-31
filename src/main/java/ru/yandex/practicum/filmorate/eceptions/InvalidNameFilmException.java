package ru.yandex.practicum.filmorate.eceptions;

public class InvalidNameFilmException extends Throwable {
    public InvalidNameFilmException(String msg) {
        super(msg);
    }
}
