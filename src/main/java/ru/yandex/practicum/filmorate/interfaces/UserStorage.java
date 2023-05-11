package ru.yandex.practicum.filmorate.interfaces;

import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {

    Collection<User> findAllUsers();

    User addUser(User user) throws UsersEmailCondition, UsersLoginCondition, UserDateBirthdayException, UsersEmptyEmailCondition, UsersEmptyLoginCondition;

    Map<Integer, User> getUsers();
    User findUserById (int id);
    User updateUser(User user) throws UserDoesNotExist;
    Collection<User> findListUserFriends(int userId);
    Collection<User> findCommonFriends(int userId, int otherId);
    User addUserFriend(int userId, int friendId);
    User removeUserFriend(int id, int friendId);



}
