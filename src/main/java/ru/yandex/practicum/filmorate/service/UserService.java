package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.managers.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private final InMemoryUserStorage storage;

    @Autowired
    public UserService(InMemoryUserStorage storage) {
        this.storage = storage;
    }

    public List<User> showListUserFriends(int id){
        List<User> users = new ArrayList<>();
        var listFriends = storage.getUsers().get(id).getFriends();
        for(Integer userId : listFriends){
            users.add(storage.getUsers().get(userId));
        }
        return users;
    }

    public List<User> findCommonFriends(int id, int otherId){
        List<User> users = new ArrayList<>();
        User mainUser = storage.getUsers().get(id);
        User secondUser = storage.getUsers().get(otherId);
        for(Integer friends: mainUser.getFriends()){
            if(secondUser.getFriends().contains(friends)){
                users.add(storage.getUsers().get(friends));
            }
        }
        return users;
    }

    public User addUserFriend(int id, int friendId){
        storage.getUsers().get(id).addFriend(friendId);
        storage.getUsers().get(friendId).addFriend(id);
        return storage.getUsers().get(friendId);
    }

    public User removeUserFriend(int id, int friendId){
        storage.getUsers().get(id).removeFriend(friendId);
        return storage.getUsers().get(friendId);
    }


}
