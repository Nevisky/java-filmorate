package ru.yandex.practicum.filmorate.exceptions;

public class UserDoesNotExist extends RuntimeException  {
    public UserDoesNotExist(String msg) {
        super(msg);
    }
}
