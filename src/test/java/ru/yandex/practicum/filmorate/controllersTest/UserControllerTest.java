package ru.yandex.practicum.filmorate.controllersTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.model.User;


import java.time.LocalDate;
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerTest {

    private final UserStorage userController;

    private User user;


    @BeforeEach
    void beforeEach() {
        user = User.builder().name("Мотя").login("Login").id(1).email("yandex@mail.ru").birthday(LocalDate.of(2020, 2, 20)).build();
    }

    @Test
    void validateNameTest() throws UsersLoginCondition, UsersEmailCondition, UserDateBirthdayException, UsersEmptyEmailCondition, UsersEmptyLoginCondition {
        user.setName("");
        try {
            userController.addUser(user);
            Assertions.assertEquals(user.getName(), user.getLogin());
        } catch (UsersNameCondition e) {
            Assertions.assertEquals("Имя для отображения может быть пустым — в таком случае будет использован логин", e.getMessage());
        }

    }

    @Test
    void validateEmptyLoginTest() throws UsersEmailCondition, UserDateBirthdayException, UsersEmptyEmailCondition, UsersLoginCondition {
        user.setLogin("");
        try {
            userController.addUser(user);
            Assertions.fail();
        } catch (UsersEmptyLoginCondition e) {
            Assertions.assertEquals("Логин не может быть пустым.", e.getMessage());
        }

    }

    @Test
    void validateSpaceLoginTest() throws UsersEmailCondition, UserDateBirthdayException, UsersEmptyEmailCondition, UsersEmptyLoginCondition {
        user.setLogin("Login Login");
        try {
            userController.addUser(user);
            Assertions.fail();
        } catch (UsersLoginCondition e) {
            Assertions.assertEquals("Логин не может содержать пробелы.", e.getMessage());
        }

    }

    @Test
    void validateEmptyEmailTest() throws UsersLoginCondition, UserDateBirthdayException, UsersEmailCondition, UsersEmptyLoginCondition {

        user.setEmail(" ");

        try {
            userController.addUser(user);
            Assertions.fail();
        } catch (UsersEmptyEmailCondition e) {
            Assertions.assertEquals("Электронная почта не может быть пустой.", e.getMessage());
        }
    }

    @Test
    void validateSymbolEmailTest() throws UsersLoginCondition, UserDateBirthdayException, UsersEmptyEmailCondition, UsersEmptyLoginCondition {

        user.setEmail("yandex|mail.ru");

        try {
            userController.addUser(user);
            Assertions.fail();
        } catch (UsersEmailCondition e) {
            Assertions.assertEquals("Электронная почта должна содержать символ @", e.getMessage());
        }
    }


    @Test
    void validateBirthdayTest() throws UsersLoginCondition, UsersEmailCondition, UsersEmptyLoginCondition {
        user.setBirthday(LocalDate.now().plusYears(2));
        try {
            userController.addUser(user);
            Assertions.fail();
        } catch (UserDateBirthdayException | UsersEmptyEmailCondition e) {
            Assertions.assertEquals("Дата рождения не может быть в будущем", e.getMessage());
        }
    }

}
