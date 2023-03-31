package ru.yandex.practicum.filmorate.eceptions;

public class UserAlreadyExistException extends Throwable {
    public UserAlreadyExistException(String msg) {
        super(msg);
    }
}
