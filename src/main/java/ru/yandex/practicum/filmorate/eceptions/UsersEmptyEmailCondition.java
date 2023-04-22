package ru.yandex.practicum.filmorate.eceptions;

public class UsersEmptyEmailCondition extends RuntimeException {
    public UsersEmptyEmailCondition(String msg) {
        super(msg  );
    }
}
