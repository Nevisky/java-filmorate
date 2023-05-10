package ru.yandex.practicum.filmorate.dbStorageTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserDbStorageTest {
    private final UserStorage userStorage;


    private User userTest1;
    private User userTest2;
    private User userTest3;

    @BeforeEach
    void setUp() {
        userTest1 = User.builder()
                .name("Пользователь 1")
                .login("UserLogin")
                .email("user1@mail.ru")
                .birthday(LocalDate.of(2000, 3, 22))
                .build();

        userTest2 = User.builder()
                .name("Пользователь 2")
                .login("UserLogin2")
                .email("user2@mail.ru")
                .birthday(LocalDate.of(2002, 7, 30))
                .build();


        userTest3 = User.builder()
                .name("Пользователь 3")
                .login("UserLogin3")
                .email("user3@mail.ru")
                .birthday(LocalDate.of(2002, 12, 12))
                .build();
    }
    @Test
    void testCreateUser() {
        User user = userStorage.addUser(userTest1);
        User newUser = userStorage.findUserById(user.getId());

        Assertions.assertEquals(user,newUser);
    }

    @Test
    void testUpdateUser() {
        User user = userStorage.addUser(userTest1);
        user.setName("Гога");
        user.setLogin("Gera");
        User exUser = userStorage.updateUser(user);

        assertThat(exUser).hasFieldOrPropertyWithValue("name", "Гога")
                .hasFieldOrPropertyWithValue("login", "Gera");

    }
    @Test
    void testFindUsers() {
        User user = userStorage.addUser(userTest1);
        User user2 = userStorage.addUser(userTest2);
        Collection<User> usersList = userStorage.findAllUsers();

        assertThat(usersList).hasSize(2).contains(user, user2);
    }

    @Test
    void testFindUserById() {
        User user = userStorage.addUser(userTest1);
        User findedUser = userStorage.findUserById(user.getId());

        assertThat(findedUser).isEqualTo(user);
    }

    @Test
    void testAddFriend() {
        User user = userStorage.addUser(userTest1);
        User user1 = userStorage.addUser(userTest2);
        User userWithFriend = userStorage.addUserFriend(user.getId(), user1.getId());

        Collection<User> friends = userStorage.findListUserFriends(userWithFriend.getId());

        assertThat(friends).hasSize(1)
                .contains(user1);
    }

    @Test
    void testGetFriendsUser() {
        User user = userStorage.addUser(userTest1);
        User user1 = userStorage.addUser(userTest2);
        User userWithFriend= userStorage.addUserFriend(user.getId(),user1.getId());
        Collection<Integer> friends = userWithFriend.getFriends();

        assertThat(friends).hasSize(1).contains(2);
    }

    @Test
    void testCommonFriends() {
        User user1 = userStorage.addUser(userTest1);
        User user2 = userStorage.addUser(userTest2);
        User user3 = userStorage.addUser(userTest3);
        User friendsUser1 = userStorage.addUserFriend(user1.getId(), user3.getId());
        User friendsUser2 = userStorage.addUserFriend(user2.getId(), user3.getId());
        Collection<User> commonFriends = userStorage.findCommonFriends(friendsUser1.getId(),friendsUser2.getId());

        assertThat(commonFriends).hasSize(1).contains(user3);
    }



    @Test
    void testDeleteFriend() {
        User user = userStorage.addUser(userTest1);
        User user1 = userStorage.addUser(userTest2);
        User user1WithFriend = userStorage.addUserFriend(user.getId(), user1.getId());
        userStorage.removeUserFriend(user1WithFriend.getId(), user1.getId());

        Collection<User> friends = userStorage.findListUserFriends(user1WithFriend.getId());

        assertThat(friends).isEmpty();
    }


}