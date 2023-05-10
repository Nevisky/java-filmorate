package ru.yandex.practicum.filmorate.dbStorageTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.FilmDbStorageDao;
import ru.yandex.practicum.filmorate.dao.GenreDbStorageDao;
import ru.yandex.practicum.filmorate.dao.MpaDbStorageDao;
import ru.yandex.practicum.filmorate.dao.UserDbStorageDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmDbStorageTest {
    private final FilmDbStorageDao filmDbStorage;
    private final UserDbStorageDao userDbStorage;
    private final GenreDbStorageDao genreStorage;
    private final MpaDbStorageDao mpaStorage;
    private Film filmTest1;
    private Film filmTest2;
    private User userTest1;
    private User userTest2;

    @BeforeEach
    void setUp() {
        filmTest1 = Film.builder()
                .mpa(Mpa.builder()
                        .id(1)
                        .build())
                .name("Первый Фильм")
                .description("Описание первого фильма ")
                .releaseDate(LocalDate.of(2000, 2, 2))
                .duration(130)
                .build();

        filmTest2 = Film.builder()
                .mpa(Mpa.builder()
                        .id(2)
                        .build())
                .name("Второй фильм")
                .description("Описание второго фильма ")
                .releaseDate(LocalDate.of(2001,1 , 1))
                .duration(210)
                .build();

        userTest1 = User.builder()
                .name("Пользователь 1")
                .login("UserLogin")
                .email("user1@mail.ru")
                .birthday(LocalDate.of(2000, 3, 22))
                .build();

        userTest2 = User.builder()
                .name("Пользователь 2")
                .login("UserLogin")
                .email("user2@mail.ru")
                .birthday(LocalDate.of(2002, 7, 30))
                .build();
    }
    @Test
    void testCreateFilm() {
        Film film = filmDbStorage.addFilm(filmTest1);
        Film testFilm = filmDbStorage.findFilm(film.getId());

        assertThat(testFilm).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    void testGetFilms() {
        Film film1 = filmDbStorage.addFilm(filmTest1);
        Film film2 = filmDbStorage.addFilm(filmTest2);
        Collection<Film> filmsList = filmDbStorage.findAllFilms();

        assertThat(filmsList).contains(film1, film2);
    }

    @Test
    void testFindFilmById() {
        Film createFilm = filmDbStorage.addFilm(filmTest1);
        Film findedFilm  = filmDbStorage.findFilm(filmTest1.getId());
        Assertions.assertEquals(createFilm,findedFilm);
    }

    @Test
    void testUpdateFilm() {
        Film film = filmDbStorage.addFilm(filmTest1);
        film.setName("Update название");
        film.setDescription("Update описание");
        Set<Genre> genresList = new HashSet<>();
        genresList.add(Genre.builder().id(1).build());
        genresList.add(Genre.builder().id(2).build());
        genresList.add(Genre.builder().id(3).build());
        film.setGenres(genresList);
        Film filmEx = filmDbStorage.updateFilm(film);

        assertThat(filmEx)
                .hasFieldOrPropertyWithValue("name", "Update название")
                .hasFieldOrPropertyWithValue("description", "Update описание");

        assertThat(filmEx.getGenres()).hasSize(3);
    }
    @Test
    void testGetFilmsWithCountedLikes() {
        Film film = filmDbStorage.addFilm(filmTest1);
        Film film2 = filmDbStorage.addFilm(filmTest2);
        User user = userDbStorage.addUser(userTest1);
        User user2 = userDbStorage.addUser(userTest2);
        filmDbStorage.addLike(film.getId(),user.getId());
        Film filmWithLikes1 = filmDbStorage.addLike(film.getId(),user2.getId());
        Film filmWithLikes2 = filmDbStorage.addLike(film2.getId(),user.getId());
        Collection<Film> popularFilms = filmDbStorage.findPopularFilm(null);

        assertThat(popularFilms)
                .hasSize(2)
                .contains(filmWithLikes1, filmWithLikes2);
    }

    @Test
    void testLikeFilm() {
        Film film = filmDbStorage.addFilm(filmTest1);
        User user = userDbStorage.addUser(userTest1);
        film.like(user.getId());
        assertThat(film.getLikes()).contains(1);
    }

    @Test
    void testDeleteLike() {
        Film film = filmDbStorage.addFilm(filmTest1);
        User user = userDbStorage.addUser(userTest1);
        film.like(user.getId());
        filmDbStorage.removeLike(film.removeLikes(user.getId()),user.getId());

        assertThat(film.getLikes()).isEmpty();
    }
    @Test
    void testGetGenreById() {
        Genre genre = genreStorage.getGenreById(1);
        Genre genre2 = genreStorage.getGenreById(2);

        assertThat(genre).hasFieldOrPropertyWithValue("name", "Комедия");
        assertThat(genre2).hasFieldOrPropertyWithValue("name", "Драма");
    }

    @Test
    void testGetGenresList() {
        Collection<Genre> genreList = genreStorage.getGenresList();
        assertThat(genreList).hasSize(6);
    }

    @Test
    void testGetMpaList() {
        Collection<Mpa> mpaList = mpaStorage.getMpaList();

        assertThat(mpaList).hasSize(5);
    }

    @Test
    void testGetMpaById() {
        Mpa mpa1 = mpaStorage.getMpaById(1);

        assertThat(mpa1).hasFieldOrPropertyWithValue("name", "G");
    }

}