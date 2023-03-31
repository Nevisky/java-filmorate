package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Bean;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {

    User firstUser = User.builder()
            .id(1)
            .email("mail@yandex.ru")
            .login("dolore")
            .name("Nick Name")
            .birthday(LocalDate.of(1946,8,20))
            .build();
    @Autowired
    protected static Validator validator;
    @BeforeAll
    @Bean
    public static void validInput() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    @DisplayName("email is Empt")
    public void shouldGetError() {
        firstUser.setEmail("");
       var check =  validator.validate(firstUser);
        assertFalse(check.isEmpty(), "Пустое значение поля «email».");
    }

    @Test
    void validate() {
        Set<ConstraintViolation<User>> violations = validator.validate(firstUser);
        assertTrue(violations.isEmpty());

    }

}