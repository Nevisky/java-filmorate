package ru.yandex.practicum.filmorate.model;


import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class User {
    int id;
    String email;
    String login;
    String name;
    LocalDate birthday;
}
