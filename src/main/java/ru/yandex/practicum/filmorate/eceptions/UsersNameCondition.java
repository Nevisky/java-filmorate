package ru.yandex.practicum.filmorate.eceptions;

public class UsersNameCondition extends RuntimeException {
    public UsersNameCondition(String msg) {
        super(msg);
    }
}
