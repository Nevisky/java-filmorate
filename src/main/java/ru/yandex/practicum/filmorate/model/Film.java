package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Film {
   private Integer id;
    @NotBlank
    private String name;
    @Size(min = 1, max = 200)
   private  String description;
    @NotNull
    private LocalDate releaseDate;
    @Min(1)
    private Integer duration;
    @Builder.Default
    private Set<Integer> likes = new HashSet<>();

    public int like(int userId){
        if(likes == null){
            likes = new HashSet<>();

        }
        likes.add(userId);
        return userId;
    }
    public Set<Integer> getLikes(){
        if(likes == null){
            likes = new HashSet<>();
        }
        return likes;
    }

    public int removeLikes(int userId){
        likes.remove(userId);
        return userId;
    }

}
