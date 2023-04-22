package ru.yandex.practicum.filmorate.model;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class User {
   private Integer id;
   @Email
   private  String email;
   @NonNull
   private  String login;
   private String name;
   @DateTimeFormat
   private LocalDate birthday;
   @Builder.Default
   private Set<Integer> friends = new HashSet<>();

   public int addFriend(int  friendId){
      if(friends == null){
         friends = new HashSet<>();

      }
         friends.add(friendId);
      return friendId;
      }
      public Set<Integer> getFriends(){
      if(friends == null){
         friends = new HashSet<>();
      }
      return friends;
      }

   public void removeFriend(int friendId){
      friends.remove(friendId);
      }

   }
