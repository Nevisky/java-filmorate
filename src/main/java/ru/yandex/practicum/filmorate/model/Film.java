package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
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
    private Set<Integer> likes = new HashSet<>();
    private Mpa mpa;
    private Set<Genre> genres;

    public Film(int filmId, String filmName, String filmDescription, LocalDate filmReleaseDate, int filmDuration) {
        this.id = filmId;
        this.name= filmName;
        this.description = filmDescription;
        this.releaseDate = filmReleaseDate;
        this.duration = filmDuration;
    }



    public Mpa getMpa() {
        if(mpa == null){
            setMpa(new Mpa(0,"Empty"));
        }
        return mpa;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", releaseDate=" + releaseDate +
                ", duration=" + duration +
                ", likes=" + likes +
                ", mpa=" + mpa +
                ", genres=" + genres +
                '}';
    }

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
