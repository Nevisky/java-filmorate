package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmDoesNotExist;
import ru.yandex.practicum.filmorate.interfaces.GenreStorage;
import ru.yandex.practicum.filmorate.model.Genre;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenreDbStorageDao implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getGenreById(Integer genreId) {
        SqlRowSet genreRow = jdbcTemplate.queryForRowSet("SELECT * FROM PUBLIC.GENRE " +
                "                                               WHERE GENRE_ID = ?", genreId);
        if (genreRow.next()) {
            return new Genre(
                    genreRow.getInt("GENRE_ID"),
                    genreRow.getString("GENRE_NAME"));
        } else {
            log.info("Жанр с идентификатором {} не найден.", genreId);
            throw new FilmDoesNotExist("Пустое значение, Жанр не найден");
        }
    }

    @Override
    public Collection<Genre> getGenresList() {
        Collection<Genre> genres = new ArrayList<>();
        String sqlQuery = "SELECT * FROM PUBLIC.GENRE " +
                            "ORDER BY GENRE_ID ASC";
        SqlRowSet genreRow = jdbcTemplate.queryForRowSet(sqlQuery);
        while (genreRow.next()) {
            genres.add(
                    new Genre(
                            genreRow.getInt("GENRE_ID"),
                            genreRow.getString("GENRE_NAME")));
        }
        return genres;
    }

}
