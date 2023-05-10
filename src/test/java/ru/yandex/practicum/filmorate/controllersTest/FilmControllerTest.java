package ru.yandex.practicum.filmorate.controllersTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exceptions.FilmDescriptionCouldNotBeMore200Symbols;
import ru.yandex.practicum.filmorate.exceptions.FilmDurationsMustBePositive;
import ru.yandex.practicum.filmorate.exceptions.FilmNameCouldNotBeEmpty;
import ru.yandex.practicum.filmorate.exceptions.FilmReleaseDateCouldNotBeEarlyThanCertainDate;
import ru.yandex.practicum.filmorate.managers.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;

public class FilmControllerTest {
    private final InMemoryFilmStorage storage = new InMemoryFilmStorage();
    private final FilmService service = new FilmService(storage);



    private Film film;

    @BeforeEach
    void beforeEach() {

        film = Film.builder()
                .id(1)
                .name("Фильм")
                .description("Комедия")
                .duration(120)
                .releaseDate(LocalDate.of(2000, 1, 1))
                .build();

    }

    @Test
    void validateFilmDescription() {
        
        FilmController filmController = new FilmController(storage);
        film.setDescription("Превышение количества символов/Превышение количества символов/Превышение количества символов/" +
                "Превышение количества символов/Превышение количества символов/Превышение количества символов/Превышение количества символов/" +
                "Превышение количества символов/Превышение количества символов/Превышение количества символов/Превышение количества символов/" +
                "Превышение количества символов/Превышение количества символов/Превышение количества символов/Превышение количества символов/" +
                "Превышение количества символов/Превышение количества символов/Превышение количества символов/Превышение количества символов/");

        try {
            filmController.addFilm(film);
            Assertions.fail();
        } catch (FilmDescriptionCouldNotBeMore200Symbols e) {
            Assertions.assertEquals("Описание фильма превышает 200 символов", e.getMessage());
        }

    }

    @Test
    void validateNameFilmTest() {
        FilmController filmController = new FilmController(storage);
        film.setName("");

        try {
            filmController.addFilm(film);
            Assertions.fail();
        } catch (FilmNameCouldNotBeEmpty e) {
            Assertions.assertEquals("Наименование фильма не может быть пустым", e.getMessage());
        }

    }

    @Test
    void validateDurationTest() {
        FilmController filmController = new FilmController(storage);
        film.setDuration(-100);

        try {
            filmController.addFilm(film);
            Assertions.fail();
        } catch (FilmDurationsMustBePositive e) {
            Assertions.assertEquals("Продолжительность фильма не может быть отрицательной", e.getMessage());
        }

    }

    @Test
    void validateReleaseDateTest() {
        FilmController filmController = new FilmController(storage);
        film.setReleaseDate(LocalDate.of(1666, 6, 6));
        try {
            filmController.addFilm(film);
            Assertions.fail();
        } catch (FilmReleaseDateCouldNotBeEarlyThanCertainDate e) {
            Assertions.assertEquals("Данный фильм выпущен раньше, чем 28.12.1985 года", e.getMessage());
        }
    }
}
