package ru.yandex.practicum.filmorate.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.eceptions.*;
import ru.yandex.practicum.filmorate.managers.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

@Slf4j
@RestController
public class UserController {

    private final InMemoryUserStorage inMemoryUserStorage;
    private final UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage, UserService userService){
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userService = userService;
    }

    @GetMapping("/users")
    @ResponseBody
    public Collection<User> findAllUsers() {
        log.debug("Получен запрос GET. Количество пользователей: " + inMemoryUserStorage.findAllUsers().size()) ;
        return inMemoryUserStorage.findAllUsers();
    }

    @GetMapping("/users/{id}")
    @ResponseBody
    public User findUserById(@PathVariable int id) throws UserDoesNotExist {
        log.debug("Получен запрос GET. Количество пользователей: " + inMemoryUserStorage.findAllUsers().size());
        if(inMemoryUserStorage.getUsers().get(id) == null){
            throw new UserDoesNotExist("Данного пользователя не существует");
        }
        return inMemoryUserStorage.getUsers().get(id);
        }
    @GetMapping("/users/{id}/friends")
    @ResponseBody
    public List<User> showUserFriendsList(@PathVariable int id) {
        log.debug("Получен запрос GET. У пользователя : " + inMemoryUserStorage.getUsers().get(id).getName() + " "
                + inMemoryUserStorage.getUsers().get(id).getFriends().size() + " друзей.");
        return userService.showListUserFriends(id);
    }
    @GetMapping("/users/{id}/friends/common/{otherId}")
    @ResponseBody
    public List<User> findCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.debug("Получен запрос GET. У пользователя : " + inMemoryUserStorage.getUsers().get(id).getName() + "общих друзей");
        return userService.findCommonFriends(id,otherId);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.debug("Получен запрос PUT. User " + inMemoryUserStorage.getUsers().get(id) + " - добавляет в друзья "
                + inMemoryUserStorage.getUsers().get(friendId));
        if(inMemoryUserStorage.getUsers().get(friendId) == null){
            throw new UsersFriendException("Невозможно добавить друга с данным ID " + friendId);
        }
        return userService.addUserFriend(id,friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public User removeFriend(@PathVariable int id, @PathVariable int friendId) {
        log.debug("Получен запрос DELETE. User " + inMemoryUserStorage.getUsers().get(id) + " - удаляет из друзей "
                + inMemoryUserStorage.getUsers().get(friendId));
        return userService.removeUserFriend(id,friendId);
    }


    @PostMapping(value = "/users")
    public User create(@NotNull @Valid @RequestBody User user) throws UsersEmailCondition, UsersLoginCondition,
            UserDateBirthdayException, UsersEmptyEmailCondition, UsersEmptyLoginCondition {
        log.debug("Получен запрос Post " + user) ;

        return inMemoryUserStorage.addUser(user);
    }
    @PutMapping("/users")
    public User update (@NotNull @Valid @RequestBody User user) throws UserDoesNotExist {
        log.debug("Получен запрос PUT" + user) ;
        inMemoryUserStorage.updateUser(user);
        return user;
    }


}
