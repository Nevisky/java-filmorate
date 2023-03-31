package ru.yandex.practicum.filmorate.eceptions;

public class InvalidEmailException extends Throwable {
    public InvalidEmailException(String msg) {
        super(msg);
    }
}
