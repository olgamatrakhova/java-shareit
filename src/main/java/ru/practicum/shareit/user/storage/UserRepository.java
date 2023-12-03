package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    User addUser(User user);

    User updateUserById(Integer id, User user);

    List<User> getUsers();

    User deleteUserById(Integer id);

    User getUserById(Integer id);
}