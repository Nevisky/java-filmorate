package ru.yandex.practicum.filmorate.exceptions;

public class UserCouldNotUpdate extends RuntimeException {
    public UserCouldNotUpdate(String msg) {
        super(msg);
    }
}
