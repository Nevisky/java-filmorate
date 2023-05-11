package ru.yandex.practicum.filmorate.exceptions;

public class FilmDescriptionCouldNotBeMore200Symbols extends RuntimeException {
    public FilmDescriptionCouldNotBeMore200Symbols(String msg) {
        super(msg);
    }
}
