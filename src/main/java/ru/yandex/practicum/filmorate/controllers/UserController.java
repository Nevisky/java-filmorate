package ru.yandex.practicum.filmorate.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.model.User;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserStorage userStorage;

    @GetMapping("/users")
    @ResponseBody
    public Collection<User> findAllUsers() {
        log.debug("Получен запрос GET. Количество пользователей: " + userStorage.findAllUsers().size());
        return userStorage.findAllUsers();
    }

    @GetMapping("/users/{id}")
    @ResponseBody
    public User findUserById(@PathVariable int id) throws UserDoesNotExist {
        log.debug("Получен запрос GET. Количество пользователей: " + userStorage.findAllUsers().size());
        if(userStorage.getUsers().get(id)== null){
            throw new UserDoesNotExist("Данного пользователя не существует");
        }
        return userStorage.findUserById(id);
        }
    @GetMapping("/users/{id}/friends")
    @ResponseBody
    public Collection<User> showUserFriendsList(@PathVariable int id) {
        log.debug("Получен запрос GET. У пользователя : " + userStorage.findUserById(id).getName() + " "
                + userStorage.findUserById(id).getFriends().size() + " друзей.");
        return userStorage.findListUserFriends(id);
    }
    @GetMapping("/users/{id}/friends/common/{otherId}")
    @ResponseBody
    public Collection<User> findCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.debug("Получен запрос GET. У пользователя : " + userStorage.getUsers().get(id).getName() + " общих друзей c " +
                userStorage.getUsers().get(otherId).getName() + " - " + userStorage.findCommonFriends(id,otherId).size());
        return userStorage.findCommonFriends(id,otherId);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.debug("Получен запрос PUT. User " + userStorage.getUsers().get(id) + " - добавляет в друзья "
                + userStorage.getUsers().get(friendId));
        if(userStorage.getUsers().get(friendId) == null){
            throw new UsersFriendException("Невозможно добавить друга с данным ID " + friendId);
        }
        return userStorage.addUserFriend(id,friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public User removeFriend(@PathVariable int id, @PathVariable int friendId) {
        log.debug("Получен запрос DELETE. User " + userStorage.getUsers().get(id) + " - удаляет из друзей "
                + userStorage.getUsers().get(friendId));
        return userStorage.removeUserFriend(id,friendId);
    }


    @PostMapping(value = "/users")
    public User create(@NotNull @Valid @RequestBody User user) throws UsersEmailCondition, UsersLoginCondition,
            UserDateBirthdayException, UsersEmptyEmailCondition, UsersEmptyLoginCondition {
        log.debug("Получен запрос Post " + user);

        return userStorage.addUser(user);
    }
    @PutMapping("/users")
    public User update(@NotNull @Valid @RequestBody User user) throws UserDoesNotExist {
        log.debug("Получен запрос PUT" + user) ;
        userStorage.updateUser(user);
        return user;
    }


}
