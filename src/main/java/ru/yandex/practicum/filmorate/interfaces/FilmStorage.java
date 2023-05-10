package ru.yandex.practicum.filmorate.interfaces;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;
import java.util.Map;
public interface FilmStorage {

    Collection<Film> findAllFilms();

    Film addFilm(Film film);
    Film findFilm(int filmId);
    Film updateFilm(Film film);
    Map<Integer,Film> getFilms();
    Collection <Film> findPopularFilm(Integer count);
    Film addLike(int filmId, int userId);
    Film removeLike(int filmId, int userId);



}
