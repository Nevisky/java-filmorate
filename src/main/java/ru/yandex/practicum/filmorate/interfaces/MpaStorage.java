package ru.yandex.practicum.filmorate.interfaces;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

public interface MpaStorage {

    Collection<Mpa> getMpaList();

    Mpa getMpaById(Integer ratingId);
}