package ru.yandex.practicum.filmorate.exceptions;

public class UsersEmptyLoginCondition extends RuntimeException  {
    public UsersEmptyLoginCondition(String msg) {
        super(msg);
    }
}
