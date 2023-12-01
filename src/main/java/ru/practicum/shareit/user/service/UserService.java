package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto userDto);

    UserDto updateUserById(Integer id, UserDto userDto);

    List<UserDto> getUsers();

    UserDto deleteUserById(Integer id);

    UserDto getUserById(Integer id);
}