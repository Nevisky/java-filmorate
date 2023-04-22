package ru.yandex.practicum.filmorate.eceptions;

public class UserCouldNotUpdate extends RuntimeException {
    public UserCouldNotUpdate(String msg) {
        super(msg);
    }
}
