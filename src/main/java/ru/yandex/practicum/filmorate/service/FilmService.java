package ru.yandex.practicum.filmorate.service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserDoesNotExist;
import ru.yandex.practicum.filmorate.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    @Autowired
    private final FilmStorage storage;


    public Film addLike(int filmId, int userId) {
        storage.getFilms().get(filmId).getLikes().add(userId);
        return storage.getFilms().get(userId);
    }

    public Film removeLike(int filmId, int userId) throws UserDoesNotExist {
        if(userId < 0){
            throw new UserDoesNotExist("Невозможно удалить лайк");
        }
        storage.getFilms().get(filmId).getLikes().remove(userId);
        return storage.getFilms().get(filmId);

    }
    public Collection <Film> findPopularFilm(int count){
        Comparator<Film> comparator = Comparator.comparingInt(f -> f.getLikes().size());
        return storage.findAllFilms().stream()
                .sorted(comparator.reversed())
                .limit(count)
                .collect(Collectors.toSet());
    }


}
