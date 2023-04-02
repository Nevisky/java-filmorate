package ru.yandex.practicum.filmorate.model;


import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder
public class User {
   private  int id;
   @Email
   private  String email;
   @NonNull
   private  String login;
   @NotBlank
   private String name;
   @DateTimeFormat
   private LocalDate birthday;
}
