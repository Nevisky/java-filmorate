package ru.yandex.practicum.filmorate.exceptions;

public class UsersEmptyEmailCondition extends RuntimeException {
    public UsersEmptyEmailCondition(String msg) {
        super(msg  );
    }
}
