package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class UserRepositoryImpl implements UserRepository {

    private final Map<Integer, User> usersMap = new HashMap<>();
    private Integer idUser = 1;

    @Override
    public User addUser(User user) {
        user.setId(idUser);
        usersMap.put(idUser++, user);
        return user;
    }

    public User updateUserById(Integer id, User user) {
        if (!usersMap.containsKey(id)) {
            log.warn("Не возможно обновить пользователя {} (", user);
            throw new UserNotFoundException("Нет пользователя с id = " + id);
        }
        User updateUser = usersMap.get(id);
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            updateUser.setEmail(user.getEmail());
        }
        if (user.getName() != null && !user.getName().isBlank()) {
            updateUser.setName(user.getName());
        }
        return updateUser;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(usersMap.values());
    }

    @Override
    public User deleteUserById(Integer id) {
        if (usersMap.containsKey(id)) {
            return usersMap.remove(id);
        }
        log.warn("Не возможно удалить пользователя с id = {} ", id);
        throw new UserNotFoundException("Нет пользователя с id = " + id);
    }

    @Override
    public User getUserById(Integer id) {
        if (usersMap.containsKey(id)) {
            return usersMap.get(id);
        }
        throw new UserNotFoundException("Нет пользователя с id = " + id);
    }
}