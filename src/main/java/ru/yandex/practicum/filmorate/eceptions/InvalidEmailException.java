package ru.yandex.practicum.filmorate.eceptions;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String msg) {
        super(msg);
    }
}
