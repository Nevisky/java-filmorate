package ru.yandex.practicum.filmorate.eceptions;

public class FilmDescriptionCouldNotBeMore200Symbols extends Throwable {
    public FilmDescriptionCouldNotBeMore200Symbols(String msg) {
        super(msg);
    }
}
