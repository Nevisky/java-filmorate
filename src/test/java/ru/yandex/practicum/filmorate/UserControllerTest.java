package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.eceptions.UserDateBirthdayException;
import ru.yandex.practicum.filmorate.eceptions.UsersEmailCondition;
import ru.yandex.practicum.filmorate.eceptions.UsersLoginCondition;
import ru.yandex.practicum.filmorate.eceptions.UsersNameCondition;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserControllerTest {
    UserController userController = new UserController();
    private User user;

    @BeforeEach
    void beforeEach() {
        user = User.builder().name("Мотя").login("Login").id(1).email("yandex@mail.ru").birthday(LocalDate.of(2020, 2, 20)).build();
    }

    @Test
    void validateNameTest() throws UsersLoginCondition, UsersEmailCondition, UserDateBirthdayException {
        user.setName(null);
        try {
            userController.create(user);
            Assertions.assertEquals(user.getName(), user.getLogin());
        } catch (UsersNameCondition e) {
            Assertions.assertEquals("Имя для отображения может быть пустым — в таком случае будет использован логин", e.getMessage());
        }

    }

    @Test
    void validateLoginTest() throws UsersEmailCondition, UserDateBirthdayException {
        user.setLogin("");
        try {
            userController.create(user);
            Assertions.fail();
        } catch (UsersLoginCondition e) {
            Assertions.assertEquals("Логин не может быть пустым и содержать пробелы", e.getMessage());
        }

    }

    @Test
    void validateEmailTest() throws UsersLoginCondition, UserDateBirthdayException {

        user.setEmail("yandex|mail.ru");

        try {
            userController.create(user);
            Assertions.fail();
        } catch (UsersEmailCondition e) {
            Assertions.assertEquals("Электронная почта не может быть пустой и должна содержать символ @", e.getMessage());
        }
    }


    @Test
    void validateBirthdayTest() throws UsersLoginCondition, UsersEmailCondition {
        user.setBirthday(LocalDate.now().plusYears(2));
        try {
            userController.create(user);
            Assertions.fail();
        } catch (UserDateBirthdayException e) {
            Assertions.assertEquals("Дата рождения не может быть в будущем", e.getMessage());
        }
    }

}
