package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilmDbStorageDao implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Film> findAllFilms() {
        Collection<Film> films = new ArrayList<>();
        Film film;
        String sql = "SELECT FILMS.FILM_ID, film_name," +
                " film_description, film_release_date," +
                " film_duration,ID_FILM_RATING,NAME_RATING,G.GENRE_ID,GENRE_NAME " +
                "FROM PUBLIC.FILMS " +
                "LEFT join PUBLIC.FILM_RATING FR on FILMS.FILM_ID = FR.FILM_ID " +
                "LEFT join PUBLIC.RATING R on R.RATING_ID = FR.ID_FILM_RATING " +
                "LEFT JOIN PUBLIC.FILM_GENRE FG on FILMS.FILM_ID = FG.FILM_ID " +
                "LEFT JOIN PUBLIC.GENRE G on G.GENRE_ID = FG.GENRE_ID";
        SqlRowSet filmRow = jdbcTemplate.queryForRowSet(sql);
        while (filmRow.next()) {

                    film =  Film.builder().id(
                                    filmRow.getInt("FILM_ID"))
                            .name(
                                    Objects.requireNonNull(filmRow.getString("FILM_NAME")))
                            .description(
                                    Objects.requireNonNull(filmRow.getString("FILM_DESCRIPTION")))
                            .releaseDate(
                                    Objects.requireNonNull(filmRow.getDate("FILM_RELEASE_DATE")).toLocalDate())
                            .duration(
                                    filmRow.getInt("FILM_DURATION"))
                            .mpa(Mpa.builder().id(filmRow.getInt("ID_FILM_RATING"))
                                    .name(filmRow.getString("NAME_RATING")).build())
                            .genres(Collections.singleton(Genre.builder().id(filmRow.getInt("GENRE_ID"))
                                    .name(filmRow.getString("GENRE_NAME")).build())).build();
                        films.add(film);
                        updateGenre(film, film.getId());
        }
        return films;
    }

    private void updateGenre(Film film, int filmId) {
        String sqlDel = "DELETE FROM FILM_GENRE " +
                "           WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlDel, film.getId());
        if (film.getGenres() == null) {
            String sqlDelete = "DELETE FROM FILM_GENRE " +
                                "WHERE FILM_ID = ?";
            jdbcTemplate.update(sqlDelete, filmId);
            film.setGenres(Collections.emptySet());
        } else if (film.getGenres().isEmpty()) {
            film.setGenres(Collections.emptySet());
    } else {
            List<Integer> genreIds = film.getGenres().stream().map(Genre::getId).toList();
            for(Integer e : genreIds){
                if(e == 0){
                    String sqlDelete = "DELETE FROM FILM_GENRE " +
                                        "WHERE FILM_ID = ? ";
                    jdbcTemplate.update(sqlDelete, film.getId());
                    film.setGenres(Collections.emptySet());
                    return;
                }
            }
            for(Integer e : genreIds) {
                String sqlInsert = "INSERT INTO PUBLIC.FILM_GENRE (film_id, genre_id) " +
                        "           VALUES (?,?)";
                jdbcTemplate.update(sqlInsert, filmId, e);

            }

        }


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

        String sqlFilm = "INSERT INTO PUBLIC.FILMS(film_name, film_description," +
                            "film_release_date, film_duration)" +
                "VALUES (?,?,?,?)";
        String sqlRating = "INSERT INTO PUBLIC.FILM_RATING(id_film_rating) VALUES (?)";
        jdbcTemplate.update(sqlFilm, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration());
        jdbcTemplate.update(sqlRating, film.getMpa().getId());

        SqlRowSet filmRow = jdbcTemplate.queryForRowSet("SELECT FILMS.FILM_ID,RATING_ID,NAME_RATING " +
                "FROM PUBLIC.FILMS " +
                "LEFT JOIN PUBLIC.FILM_RATING FR on FILMS.FILM_ID = FR.FILM_ID " +
                "LEFT JOIN PUBLIC.RATING R on FR.ID_FILM_RATING = R.RATING_ID " +
                "ORDER BY FILM_ID " +
                "DESC LIMIT 1");

        if (filmRow.next()) {
            film.setId(filmRow.getInt("FILM_ID"));
            film.setMpa(new Mpa(filmRow.getInt("RATING_ID"),filmRow.getString("NAME_RATING")));
            updateGenre(film, film.getId());
            return new Film(film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(),
                    film.getDuration(), film.getLikes(), film.getMpa(), film.getGenres());
        } else {
            throw new FilmAlreadyExistException("Уже добавлен");
        }
    }
    @Override
    public Film findFilm(int filmId) {
        Film findedFilm;
        String sql = "select FILMS.FILM_ID, film_name, film_description, " +
                "film_release_date, film_duration,ID_FILM_RATING,NAME_RATING," +
                "G.GENRE_ID,GENRE_NAME,count(FL.FILM_ID) AS LIKED " +
                "FROM PUBLIC.FILMS " +
                "JOIN PUBLIC.FILM_RATING FR on PUBLIC.FILMS.FILM_ID = FR.FILM_ID " +
                "LEFT JOIN PUBLIC.RATING R on R.RATING_ID = FR.ID_FILM_RATING " +
                "LEFT JOIN PUBLIC.FILM_GENRE FG on FILMS.FILM_ID = FG.FILM_ID " +
                "LEFT JOIN PUBLIC.GENRE G on G.GENRE_ID = FG.GENRE_ID " +
                "LEFT JOIN PUBLIC.FILMS_LIKED FL on FILMS.FILM_ID = FL.FILM_ID  " +
                "WHERE FILMS.FILM_ID = ? " +
                "GROUP BY FILMS.FILM_ID,G.GENRE_ID";
        SqlRowSet filmRow = jdbcTemplate.queryForRowSet(sql,filmId);
        if (filmRow.next()) {
              findedFilm = Film.builder().id(
                            filmRow.getInt("FILM_ID"))
                    .name(
                            Objects.requireNonNull(filmRow.getString("FILM_NAME")))
                    .description(
                            Objects.requireNonNull(filmRow.getString("FILM_DESCRIPTION")))
                    .releaseDate(
                            Objects.requireNonNull(filmRow.getDate("FILM_RELEASE_DATE")).toLocalDate())
                    .duration(
                            filmRow.getInt("FILM_DURATION"))
                      .likes(setLikes(filmId))
                    .mpa(Mpa.builder().id(filmRow.getInt("ID_FILM_RATING")).name(filmRow.getString("NAME_RATING")).build())
                     .build();
        } else {
            throw new FilmDoesNotExist("Фильм не найден");
        }
        String genreList = "SELECT FILM_ID,G.GENRE_ID,GENRE_NAME " +
                "FROM PUBLIC.FILM_GENRE " +
                "LEFT JOIN PUBLIC.GENRE G on FILM_GENRE.GENRE_ID = G.GENRE_ID  " +
                "WHERE FILM_ID =? " +
                "ORDER BY GENRE_ID ASC";
        SqlRowSet genres = jdbcTemplate.queryForRowSet(genreList,filmId);
        Set<Genre> filmGenres = new HashSet<>();
        while (genres.next()) {
            filmGenres.add(new Genre(genres.getInt("GENRE_ID"), (genres.getString("GENRE_NAME"))));
        }
        findedFilm.setGenres(filmGenres);

        return findedFilm;
    }

    @Override
    public Film updateFilm(Film film) {
        Film update;
        String sqlFilm = "UPDATE PUBLIC.FILMS " +
                            "SET FILM_NAME = ?,FILM_DESCRIPTION = ?, " +
                            "FILM_RELEASE_DATE = ?, FILM_DURATION= ? " +
                            "WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlFilm, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getId());
        String sqlRating = "UPDATE PUBLIC.FILM_RATING " +
                            "SET ID_FILM_RATING = ? " +
                            "WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlRating, film.getMpa().getId(), film.getId());

        SqlRowSet filmRow = jdbcTemplate.queryForRowSet("SELECT PUBLIC.FILMS.FILM_ID,FILM_NAME,FILM_DESCRIPTION," +
                "FILM_RELEASE_DATE,FILM_DURATION,FR.ID_FILM_RATING,G.GENRE_ID,GENRE_NAME " +
                "FROM PUBLIC.FILMS JOIN PUBLIC.FILM_RATING FR on FILMS.FILM_ID = FR.FILM_ID " +
                "LEFT JOIN PUBLIC.FILM_GENRE FG on FILMS.FILM_ID = FG.FILM_ID " +
                "LEFT JOIN PUBLIC.GENRE G on G.GENRE_ID = FG.GENRE_ID " +
                "WHERE FILMS.FILM_ID = ?", film.getId());
        if (filmRow.next()) {
            update = Film.builder()
                    .id(filmRow.getInt("FILM_ID"))
                    .name(filmRow.getString("FILM_NAME"))
                    .description((filmRow.getString("FILM_DESCRIPTION")))
                    .releaseDate(filmRow.getDate("FILM_RELEASE_DATE").toLocalDate())
                    .duration(filmRow.getInt("FILM_DURATION"))
                    .mpa(Mpa.builder().id(filmRow.getInt("ID_FILM_RATING")).build()).genres(film.getGenres()).build();
            updateGenre(update, update.getId());

        } else {
            log.error("Фильм с id {} не найден.", film.getId());
            throw new FilmDoesNotExist("Данного фильма не существует, невозможно обновить данные");
        }
        String genreSet = "SELECT FILM_ID,G.GENRE_ID,GENRE_NAME " +
                "FROM PUBLIC.FILM_GENRE " +
                "LEFT JOIN PUBLIC.GENRE G on FILM_GENRE.GENRE_ID = G.GENRE_ID " +
                "WHERE FILM_ID =? " +
                "ORDER BY GENRE_ID ASC";

        SqlRowSet genres = jdbcTemplate.queryForRowSet(genreSet,film.getId());
        Set<Genre> filmGenres = new HashSet<>();
        while (genres.next()) {
            filmGenres.add(new Genre(genres.getInt("GENRE_ID"), (genres.getString("GENRE_NAME"))));
        }
        update.setGenres(filmGenres);

        return update;
    }


    @Override
    public Map<Integer, Film> getFilms() {
        Map<Integer,Film> filmsList = new HashMap<>();
        String sql = "SELECT FILMS.FILM_ID, film_name, film_description," +
                " film_release_date, film_duration,ID_FILM_RATING," +
                "NAME_RATING,G.GENRE_ID,GENRE_NAME " +
                "FROM PUBLIC.FILMS " +
                "JOIN PUBLIC.FILM_RATING FR on FILMS.FILM_ID = FR.FILM_ID " +
                "LEFT JOIN PUBLIC.RATING R on R.RATING_ID = FR.ID_FILM_RATING " +
                "LEFT JOIN PUBLIC.FILM_GENRE FG on FILMS.FILM_ID = FG.FILM_ID " +
                "LEFT JOIN PUBLIC.GENRE G on G.GENRE_ID = FG.GENRE_ID";
        SqlRowSet filmRow = jdbcTemplate.queryForRowSet(sql);
        while (filmRow.next()){
            Film film = Film.builder()
                    .id(filmRow.getInt("FILM_ID"))
                    .name(filmRow.getString("FILM_NAME"))
                    .description((filmRow.getString("FILM_DESCRIPTION")))
                    .releaseDate(filmRow.getDate("FILM_RELEASE_DATE").toLocalDate())
                    .duration(filmRow.getInt("FILM_DURATION"))
                    .mpa(Mpa.builder().id(filmRow.getInt("ID_FILM_RATING")).build())
                    .genres(Collections.singleton(Genre.builder().id(filmRow.getInt("GENRE_ID"))
                            .name(filmRow.getString("GENRE_NAME")).build())).build();

            updateGenre(film,film.getId());
            filmsList.put(filmRow.getInt("FILM_ID"),film);

        }
        return filmsList;
    }

    @Override
    public Collection<Film> findPopularFilm(Integer count) {
        if(count == null){
            count = 10;
        }
        Film film;
        Collection<Film> popularFilms = new ArrayList<>();
        String sql = "select DISTINCT FILMS.FILM_ID, film_name, film_description," +
                " film_release_date, film_duration,ID_FILM_RATING,NAME_RATING," +
                "GENRE_NAME,FG.GENRE_ID,GENRE_NAME,COUNT(FL.FILM_ID) AS LIKES  " +
                "FROM PUBLIC.FILMS " +
                "LEFT JOIN PUBLIC.FILM_RATING FR on FILMS.FILM_ID = FR.FILM_ID " +
                "LEFT join PUBLIC.RATING R on R.RATING_ID = FR.ID_FILM_RATING " +
                "LEFT JOIN PUBLIC.FILM_GENRE FG on FILMS.FILM_ID = FG.FILM_ID " +
                "LEFT JOIN PUBLIC.GENRE G on G.GENRE_ID = FG.GENRE_ID " +
                "LEFT JOIN PUBLIC.FILMS_LIKED FL on FILMS.FILM_ID = FL.FILM_ID " +
                "GROUP BY FILMS.FILM_ID " +
                "ORDER BY COUNT(FL.FILM_ID) desc LIMIT ?";

        SqlRowSet filmRow = jdbcTemplate.queryForRowSet(sql, count);
        while (filmRow.next()) {
                   film = Film.builder().id(
                                    filmRow.getInt("FILM_ID"))
                            .name(
                                    Objects.requireNonNull(filmRow.getString("FILM_NAME")))
                            .description(
                                    Objects.requireNonNull(filmRow.getString("FILM_DESCRIPTION")))
                            .releaseDate(
                                    Objects.requireNonNull(filmRow.getDate("FILM_RELEASE_DATE")).toLocalDate())
                            .duration(
                                    filmRow.getInt("FILM_DURATION"))
                           .likes(setLikes(filmRow.getInt("FILM_ID")))
                            .mpa(Mpa.builder().id(filmRow.getInt("ID_FILM_RATING"))
                                    .name(filmRow.getString("NAME_RATING")).build()).build();

            Set<Genre> filmGenres = new HashSet<>();
            filmGenres.add(new Genre(filmRow.getInt("GENRE_ID"), (filmRow.getString("GENRE_NAME"))));
            film.setGenres(filmGenres);
            updateGenre(film, film.getId());
            popularFilms.add(film);
        }
        return popularFilms;

    }
    private Set<Integer> setLikes(int filmId){
        Set<Integer> likes = new HashSet<>();
        String sqlLikes = "SELECT * FROM PUBLIC.FILMS_LIKED " +
                            "WHERE FILM_ID =?";
        SqlRowSet likesRow = jdbcTemplate.queryForRowSet(sqlLikes,filmId);
        while (likesRow.next()){
            likes.add(likesRow.getInt("USER_ID"));
        }
        return likes;
    }

    @Override
    public Film addLike(int filmId,int userId) {
        String sqlString = "INSERT INTO PUBLIC.FILMS_LIKED (USER_ID, FILM_ID) VALUES (?,?)";
        jdbcTemplate.update(sqlString,userId,filmId);
        Film likedFilm = (findFilm(filmId));
        likedFilm.like(userId);
        return likedFilm;

    }

    @Override
    public Film removeLike(int filmId, int userId) {
        if(userId < 0) {
            throw new UserDoesNotExist("Невозможно удалить лайк");
        }
        String sqlString = "DELETE FROM PUBLIC.FILMS_LIKED " +
                "WHERE USER_ID = ? " +
                "AND FILM_ID = ?";
        jdbcTemplate.update(sqlString,userId,filmId);
        Film delLikedFilm = (findFilm(filmId));
        delLikedFilm.removeLikes(userId);

        return delLikedFilm;
    }
}
