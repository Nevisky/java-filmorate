package ru.yandex.practicum.filmorate.exceptions;

public class UsersLoginCondition extends RuntimeException  {
    public UsersLoginCondition(String msg) {
        super(msg);
    }
}
