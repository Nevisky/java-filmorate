package ru.yandex.practicum.filmorate.eceptions;

public class UserDontExist extends Throwable {
    public UserDontExist(String msg) {
        super(msg);
    }
}
