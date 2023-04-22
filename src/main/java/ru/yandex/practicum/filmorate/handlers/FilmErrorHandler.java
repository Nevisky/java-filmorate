package ru.yandex.practicum.filmorate.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.eceptions.*;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@RestControllerAdvice
public class FilmErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleFilmsNameException(final FilmNameCouldNotBeEmpty e) {
        return new ErrorResponse("Наименование фильма не может быть пустым");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleFilmsDescriptionException(final FilmDescriptionCouldNotBeMore200Symbols e) {
        return new ErrorResponse("Описание фильма не может быть больше 200 символов");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleFilmsDurationException(final FilmDurationsMustBePositive e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFilmExistException(final FilmAlreadyExistException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleFilmReleaseException(final FilmReleaseDateCouldNotBeEarlyThanCertainDate e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFilmDoesNotExist(final FilmDoesNotExist e) {
        return new ErrorResponse(e.getMessage());
    }

}
