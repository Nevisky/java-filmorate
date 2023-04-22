package ru.yandex.practicum.filmorate.eceptions;

public class UsersEmptyLoginCondition extends Throwable {
    public UsersEmptyLoginCondition(String msg) {
        super(msg);
    }
}
