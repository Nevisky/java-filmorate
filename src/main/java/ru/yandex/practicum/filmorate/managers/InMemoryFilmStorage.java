package ru.yandex.practicum.filmorate.managers;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.*;
public class InMemoryFilmStorage implements FilmStorage {

    private final List<Film> listFilms= new ArrayList<>();
    private int id;

    public Map<Integer,Film> getFilms() {
        return films;
    }

    @Override
    public Collection<Film> findPopularFilm(Integer count) {
        return null;
    }

    @Override
    public Film addLike(int filmId, int userId) {
        return null;
    }

    @Override
    public Film removeLike(int filmId, int userId) {
        return null;
    }

    private Map<Integer,Film> films = new HashMap<>();

    @Override
    public Collection<Film> findAllFilms() {
        return films.values();
    }

    @Override
    public Film findFilm(int filmId) {
        if(films.get(filmId) == null){
            throw new FilmDoesNotExist("Данного фильма не существует");
        }
        return films.get(filmId);
    }

    @Override
    public Film addFilm(Film film) {
        if (film.getName().isBlank()) {
            throw new FilmNameCouldNotBeEmpty("Наименование фильма не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new FilmDescriptionCouldNotBeMore200Symbols("Описание фильма превышает 200 символов");
        }
        if (film.getReleaseDate().isBefore(ChronoLocalDate.from(LocalDate.of(1895, 12, 28)))) {
            throw new FilmReleaseDateCouldNotBeEarlyThanCertainDate("Данный фильм выпущен раньше, чем 28.12.1985 года");
        }
        if (film.getDuration() < 0) {
            throw new FilmDurationsMustBePositive("Продолжительность фильма не может быть отрицательной");
        }
        if (films.containsValue(film)) {
            throw new FilmAlreadyExistException("Фильм уже был добавлен ранее");
        }
        film.setId(++id);

        listFilms.add(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (film == null) {
            throw new InvalidEmailException("Некорректный film");
        } else {
                if (films.containsKey(film.getId())) {

                } else {
                    throw new FilmDoesNotExist("Данного фильма не существует, невозможно обновить данные");
                }
            }
        return films.get(film.getId());
        }
    }
