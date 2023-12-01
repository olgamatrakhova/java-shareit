package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.storage.UserRepositoryImpl;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepositoryImpl userRepository;

    @Autowired
    public UserServiceImpl(UserRepositoryImpl userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        if (checkEmail(userDto.getEmail(), userDto.getId())) {
            throw new DuplicateEmailException("Пользователь с такой электронной почтой (" + userDto.getEmail() + ") уже добавлен");
        }
        return UserMapper.toUserDto(userRepository.addUser(UserMapper.toUser(userDto)));
    }

    @Override
    public List<UserDto> getUsers() {
        return UserMapper.toListUsersDto(userRepository.getUsers());
    }

    @Override
    public UserDto updateUserById(Integer id, UserDto userDto) {
        if (checkEmail(userDto.getEmail(), id)) {
            throw new DuplicateEmailException("Такая электронная почта (" + userDto.getEmail() + ") уже есть у другого пользователя");
        }
        return UserMapper.toUserDto(userRepository.updateUserById(id, UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto deleteUserById(Integer id) {
        return UserMapper.toUserDto(userRepository.deleteUserById(id));
    }

    @Override
    public UserDto getUserById(Integer id) {
        return UserMapper.toUserDto(userRepository.getUserById(id));
    }

    private boolean checkEmail(String email, Integer userId) {
        return userRepository.getUsers().stream().filter(user -> !user.getId().equals(userId)).anyMatch(user -> user.getEmail().equals(email));
    }
}