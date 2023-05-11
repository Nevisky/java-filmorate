package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmDoesNotExist;
import ru.yandex.practicum.filmorate.interfaces.MpaStorage;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class MpaDbStorageDao implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;
    @Override
    public Collection<Mpa> getMpaList() {
        Collection<Mpa> mpa = new ArrayList<>();
        String sqlQuery = "SELECT * FROM PUBLIC.RATING " +
                "           ORDER BY RATING_ID ASC";
        SqlRowSet genreRow = jdbcTemplate.queryForRowSet(sqlQuery);
        while (genreRow.next()) {
            mpa.add(
                    new Mpa(
                            genreRow.getInt("RATING_ID"),
                            genreRow.getString("NAME_RATING")));
        }
        return mpa;
    }

    @Override
    public Mpa getMpaById(Integer ratingId) {
        SqlRowSet ratingRow = jdbcTemplate.queryForRowSet("SELECT * FROM PUBLIC.RATING " +
                                                                "WHERE RATING_ID = ?", ratingId);
        if (ratingRow.next()) {
            return new Mpa(
                    ratingRow.getInt("RATING_ID"),
                    ratingRow.getString("NAME_RATING"));
        } else {
            log.info("Жанр с идентификатором {} не найден.", ratingId);
            throw new FilmDoesNotExist("Пустое значение, рейтинг не найден");
        }
    }
}
