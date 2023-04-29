package ru.yandex.practicum.filmorate.interfaces;

import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {

    Collection<User> findAllUsers();

    User addUser(User user) throws UsersEmailCondition, UsersLoginCondition, UserDateBirthdayException, UsersEmptyEmailCondition, UsersEmptyLoginCondition;
    void updateUser(User user) throws UserDoesNotExist;
    Map<Integer, User> getUsers();
}
