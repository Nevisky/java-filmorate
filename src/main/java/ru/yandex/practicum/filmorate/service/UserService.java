package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final UserStorage storage;


    public List<User> showListUserFriends(int userId){
        List<User> users = new ArrayList<>();
        var listFriends = storage.getUsers().get(userId).getFriends();
        for(Integer id : listFriends){
            users.add(storage.getUsers().get(id));
        }
        return users;
    }

    public List<User> findCommonFriends(int userId, int otherId){
        List<User> users = new ArrayList<>();
        User mainUser = storage.getUsers().get(userId);
        User secondUser = storage.getUsers().get(otherId);
        for(Integer friends: mainUser.getFriends()){
            if(secondUser.getFriends().contains(friends)){
                users.add(storage.getUsers().get(friends));
            }
        }
        return users;
    }

    public User addUserFriend(int userId, int friendId){
        storage.getUsers().get(userId).addFriend(friendId);
        storage.getUsers().get(friendId).addFriend(userId);
        return storage.getUsers().get(friendId);
    }

    public User removeUserFriend(int id, int friendId){
        storage.getUsers().get(id).removeFriend(friendId);
        return storage.getUsers().get(friendId);
    }


}
