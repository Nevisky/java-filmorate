package ru.yandex.practicum.filmorate.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.eceptions.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

@Slf4j
@RestController
@ResponseBody
public class UserController {
    private int id;

   private HashSet<User> users = new HashSet<>();

    @GetMapping("/users")
    public Collection<User> findAllUsers() {
        log.debug("Получен запрос GET. Количество пользователей: " + users.size()) ;
        return new ArrayList<>(users);
    }

    @PostMapping(value = "/users")
    public User create(@NotNull @Valid @RequestBody User user) throws UsersEmailCondition, UsersLoginCondition, UserDateBirthdayException {
        log.debug("Получен запрос Post " + user) ;
        if(user.getEmail().isBlank() || !user.getEmail().contains("@")){
            throw new UsersEmailCondition("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if(user.getLogin().isBlank() || user.getLogin().contains(" ")){
            throw new UsersLoginCondition("Логин не может быть пустым и содержать пробелы");
        }
        if(user.getName() == null){
            user.setName(user.getLogin());
            throw new UsersNameCondition("Имя для отображения может быть пустым — в таком случае будет использован логин");
        }
        if(user.getBirthday().isAfter(LocalDate.now())){
            throw new UserDateBirthdayException("Дата рождения не может быть в будущем");
        }
        user.setId(++id);
        users.add(user);
        return user;
    }
    @PutMapping("/users")
    public User update (@NotNull @Valid @RequestBody User user) throws UserDoesNotExist {
        log.debug("Получен запрос PUT" + user) ;
        if(user == null){
            throw new InvalidEmailException("Некорректный user");
        }else {
            for(User findUser: users){
                if(findUser.getId() == user.getId()){
                    findUser.setId(user.getId());
                    findUser.setName(user.getName());
                    findUser.setLogin(user.getLogin());
                    findUser.setBirthday(user.getBirthday());
                    findUser.setEmail(user.getEmail());
                }else {
                    throw new UserDoesNotExist("Данного пользователя не существует, невозможно обновить данные");
                }

            }
        }
        return user;
    }


}
