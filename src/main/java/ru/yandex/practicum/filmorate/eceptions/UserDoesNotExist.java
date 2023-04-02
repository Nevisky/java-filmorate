package ru.yandex.practicum.filmorate.eceptions;

public class UserDoesNotExist extends Throwable {
    public UserDoesNotExist(String msg) {
        super(msg);
    }
}
