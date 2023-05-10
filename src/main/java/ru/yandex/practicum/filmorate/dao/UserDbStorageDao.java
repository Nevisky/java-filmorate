package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDbStorageDao implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<User> findAllUsers() {
        Collection<User> users = new ArrayList<>();
        String sql = "SELECT * " +
                    "FROM PUBLIC.USERS";
        SqlRowSet userRow = jdbcTemplate.queryForRowSet(sql);
        while (userRow.next()) {
            users.add(
                    new User(
                            userRow.getInt("USER_ID"),
                            Objects.requireNonNull(userRow.getString("USER_EMAIL")),
                            Objects.requireNonNull(userRow.getString("USER_LOGIN")),
                            userRow.getString("USER_NAME"),
                            Objects.requireNonNull(userRow.getDate("USER_BIRTHDAY")).toLocalDate())
            );
        }
        return users;
    }

    @Override
    public User addUser(User user) throws UsersEmailCondition, UsersLoginCondition, UserDateBirthdayException, UsersEmptyEmailCondition, UsersEmptyLoginCondition {

        if (user.getEmail().isBlank()) {
            throw new UsersEmptyEmailCondition("Электронная почта не может быть пустой.");
        }
        if (!user.getEmail().contains("@")) {
            throw new UsersEmailCondition("Электронная почта должна содержать символ @");
        }
        if (user.getLogin().isBlank()) {
            throw new UsersEmptyLoginCondition("Логин не может быть пустым.");
        }

        if (user.getLogin().contains(" ")) {
            throw new UsersLoginCondition("Логин не может содержать пробелы.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new UserDateBirthdayException("Дата рождения не может быть в будущем");
        }
        String sqlString = "INSERT INTO USERS (user_name, user_email, user_login, user_birthday) VALUES (?,?,?,?)";
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        jdbcTemplate.update(sqlString, user.getName(), user.getEmail(), user.getLogin(), user.getBirthday());
        SqlRowSet userRow = jdbcTemplate.queryForRowSet("SELECT * FROM USERS " +
                                                            "WHERE USER_EMAIL = ?", user.getEmail());
        if (userRow.next()) {
            return new User(
                    userRow.getInt("USER_ID"),
                    Objects.requireNonNull(userRow.getString("USER_EMAIL")),
                    userRow.getString("USER_LOGIN"),
                    userRow.getString("USER_NAME"),
                    Objects.requireNonNull(userRow.getDate("USER_BIRTHDAY")).toLocalDate());
        } else {
            log.error("Пользователь с email {} не найден.", user.getEmail());
            throw new UsersNameCondition("Пустое значение User");
        }
    }


    @Override
    public User updateUser(User user) throws UserDoesNotExist {
        if (user == null) {
            throw new InvalidEmailException("Некорректный user");
        }

        String sql = "UPDATE PUBLIC.USERS " +
                "SET USER_NAME = ?,USER_EMAIL = ?, USER_LOGIN = ?, USER_BIRTHDAY = ? " +
                "WHERE USER_ID = ?";
        jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getLogin(), user.getBirthday(), user.getId());
        SqlRowSet userRow = jdbcTemplate.queryForRowSet("SELECT * " +
                                                            "FROM PUBLIC.USERS " +
                                                            "WHERE USER_ID = ?", user.getId());
        if (userRow.next()) {
            return User.builder()
                    .id(userRow.getInt("USER_ID"))
                    .name(userRow.getString("USER_NAME"))
                    .email(Objects.requireNonNull(userRow.getString("USER_EMAIL")))
                    .login(Objects.requireNonNull(userRow.getString("USER_LOGIN")))
                    .birthday(Objects.requireNonNull(userRow.getDate("USER_BIRTHDAY")).toLocalDate()).build();

        } else {
            log.error("Пользователь с id {} не найден.", user.getId());
            throw new UserDoesNotExist("Данного пользователя не существует, невозможно обновить данные");
        }
    }

    @Override
    public Collection<User> findListUserFriends(int userId) {
        Collection<User> friendsList = new ArrayList<>();
        String sql = "SELECT DISTINCT PUBLIC.USERS.USER_ID,USER_NAME,USER_EMAIL," +
                                                    "USER_LOGIN,USER_BIRTHDAY " +
                        "FROM PUBLIC.USERS " +
                        "RIGHT JOIN PUBLIC.FRIENDSHIP F on USERS.USER_ID = F.FRIEND_ID " +
                        "WHERE F.USER_ID = ?";

        SqlRowSet userRow = jdbcTemplate.queryForRowSet(sql, userId);
        while (userRow.next()) {
            friendsList.add(
                    new User(
                            userRow.getInt("USER_ID"),
                            Objects.requireNonNull(userRow.getString("USER_EMAIL")),
                            Objects.requireNonNull(userRow.getString("USER_LOGIN")),
                            userRow.getString("USER_NAME"),
                            Objects.requireNonNull(userRow.getDate("USER_BIRTHDAY")).toLocalDate()));

        }

        return friendsList;
    }

    @Override
    public Collection<User> findCommonFriends(int userId, int otherId) {
        Collection<User> commonFriendsList = new ArrayList<>();
        SqlRowSet userRow = jdbcTemplate.queryForRowSet("SELECT PUBLIC.USERS.USER_ID,USER_NAME,USER_EMAIL,USER_LOGIN,USER_BIRTHDAY " +
                        "FROM PUBLIC.USERS JOIN PUBLIC.FRIENDSHIP F on PUBLIC.USERS.USER_ID = F.FRIEND_ID " +
                        "and F.USER_ID= ? " +
                        "JOIN PUBLIC.FRIENDSHIP F2 on PUBLIC.USERS.USER_ID = F2.FRIEND_ID " +
                        "AND F2.USER_ID = ? " +
                        "LEFT JOIN PUBLIC.FRIENDSHIP F3  ON PUBLIC.USERS.USER_ID = F3.USER_ID", userId,otherId);
        while (userRow.next()) {
            commonFriendsList.add(new User(
                    userRow.getInt("USER_ID"),
                    Objects.requireNonNull(userRow.getString("USER_EMAIL")),
                    Objects.requireNonNull(userRow.getString("USER_LOGIN")),
                    userRow.getString("USER_NAME"),
                    Objects.requireNonNull(userRow.getDate("USER_BIRTHDAY")).toLocalDate()));
        }
        return commonFriendsList;
    }

    @Override
    public User addUserFriend(int userId, int friendId) {
        User user;
        String sql = "INSERT INTO PUBLIC.FRIENDSHIP (USER_ID, FRIEND_ID, STATUS_ID) VALUES (?,?,?)";
        jdbcTemplate.update(sql, userId, friendId, 1);
        SqlRowSet userRow = jdbcTemplate.queryForRowSet("SELECT USERS.USER_ID, USER_NAME, USER_EMAIL, " +
                                                "USER_LOGIN, USER_BIRTHDAY,FRIEND_ID " +
                "                       FROM PUBLIC.USERS  LEFT JOIN PUBLIC.FRIENDSHIP F on USERS.USER_ID = F.USER_ID " +
                                        "WHERE FRIEND_ID is not null AND F.USER_ID = ?",userId);
        if (userRow.next()) {
             user =User.builder()
                    .id(userRow.getInt("USER_ID"))
                    .name(userRow.getString("USER_NAME"))
                    .email(Objects.requireNonNull(userRow.getString("USER_EMAIL")))
                    .login(Objects.requireNonNull(userRow.getString("USER_LOGIN")))
                    .birthday(Objects.requireNonNull(userRow.getDate("USER_BIRTHDAY")).toLocalDate())
                    .friends(createFriends(userId)).build();
            return user;
        } else {
            throw new UserDoesNotExist("Нет такого друга");
        }
    }
    private Set<Integer> createFriends(int userId){
        Set<Integer> friends = new HashSet<>();
        SqlRowSet friendRow = jdbcTemplate.queryForRowSet("SELECT USERS.USER_ID, USER_NAME, USER_EMAIL," +
                                                            " USER_LOGIN, USER_BIRTHDAY,FRIEND_ID " +
                                                            "FROM PUBLIC.USERS  " +
                                                            "LEFT JOIN PUBLIC.FRIENDSHIP F on USERS.USER_ID = F.USER_ID " +
                                                            "WHERE FRIEND_ID is not null " +
                                                            "AND F.USER_ID = ?",userId);
        while (friendRow.next()){
            friends.add(friendRow.getInt("FRIEND_ID"));

        }
        return friends;
    }

    @Override
    public User removeUserFriend(int id, int friendId) {
        String sql = "DELETE FROM PUBLIC.FRIENDSHIP " +
                        "WHERE USER_ID = ? " +
                        "AND FRIEND_ID = ?";
        jdbcTemplate.update(sql, id, friendId);
        SqlRowSet userRow = jdbcTemplate.queryForRowSet("SELECT * " +
                                                            "FROM PUBLIC.USERS " +
                                                            "WHERE USER_ID = ?", friendId);
        if (userRow.next()) {
            return User.builder()
                    .id(userRow.getInt("USER_ID"))
                    .name(userRow.getString("USER_NAME"))
                    .email(Objects.requireNonNull(userRow.getString("USER_EMAIL")))
                    .login(Objects.requireNonNull(userRow.getString("USER_LOGIN")))
                    .birthday(Objects.requireNonNull(userRow.getDate("USER_BIRTHDAY")).toLocalDate())
                    .build();
        }else {
            throw new UserDoesNotExist("Нет такого друга");
        }
    }

    @Override
    public Map<Integer, User> getUsers() {
        Map <Integer,User> users = new HashMap<>();
        String sql = "SELECT * " +
                    "FROM PUBLIC.USERS";
        SqlRowSet userRow = jdbcTemplate.queryForRowSet(sql);
        while (userRow.next()) {
            users.put(userRow.getInt("USER_ID"),
                    new User(
                            userRow.getInt("USER_ID"),
                            Objects.requireNonNull(userRow.getString("USER_EMAIL")),
                            Objects.requireNonNull(userRow.getString("USER_LOGIN")),
                            userRow.getString("USER_NAME"),
                            Objects.requireNonNull(userRow.getDate("USER_BIRTHDAY")).toLocalDate())
            );
        }
        return users;
    }

    @Override
    public User findUserById(int id) {
        String sqlString = "SELECT * " +
                            "FROM PUBLIC.USERS " +
                            "WHERE USER_ID = ?";
        SqlRowSet userRow = jdbcTemplate.queryForRowSet(sqlString, id);
        if (userRow.next() && getUsers().get(id) != null) {
            return new User(userRow.getInt("USER_ID"),
                    Objects.requireNonNull(userRow.getString("USER_EMAIL")),
                    Objects.requireNonNull(userRow.getString("USER_LOGIN")),
                    userRow.getString("USER_NAME"),
                    Objects.requireNonNull(userRow.getDate("USER_BIRTHDAY")).toLocalDate());
        } else {
            log.error("Пользователь с ID {} не найден.", findUserById(id));
            throw new UserDoesNotExist("Пользователь не найден");
        }
    }
}