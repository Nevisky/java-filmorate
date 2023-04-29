package ru.yandex.practicum.filmorate.managers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private Integer id = 0;
    @Autowired
    public Map<Integer, User> getUsers() {
        return users;
    }
    private final Map<Integer,User> users = new HashMap<>();
    @Override
    public Collection<User> findAllUsers() {
        return users.values();
    }
    @Override
    public User addUser(User user) throws UsersEmailCondition, UsersLoginCondition, UserDateBirthdayException, UsersEmptyEmailCondition, UsersEmptyLoginCondition {
        if(user.getEmail().isBlank()){
            throw new UsersEmptyEmailCondition("Электронная почта не может быть пустой.");
        }
        if(!user.getEmail().contains("@")) {
            throw new UsersEmailCondition("Электронная почта должна содержать символ @");
        }
        if(user.getLogin().isBlank()){
            throw new UsersEmptyLoginCondition("Логин не может быть пустым.");
        }

        if(user.getLogin().contains(" ")){
            throw new UsersLoginCondition("Логин не может содержать пробелы.");
        }
        if(user.getName().isBlank() || user.getName() == null){
            user.setName(user.getLogin());
        }
        if(user.getBirthday().isAfter(LocalDate.now())){
            throw new UserDateBirthdayException("Дата рождения не может быть в будущем");
        }
        user.setId(++id);
        users.put(user.getId(),user);

        return user;
    }

    @Override
    public void updateUser(User user) throws UserDoesNotExist {
        if(user == null){
            throw new InvalidEmailException("Некорректный user");
        }else {
                if(users.containsKey(user.getId())){
                    users.put(user.getId(), user);
                }else {
                    throw new UserDoesNotExist("Данного пользователя не существует, невозможно обновить данные");
                }

            }
        }
    }
