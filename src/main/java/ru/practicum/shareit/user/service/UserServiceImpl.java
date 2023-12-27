package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

import static ru.practicum.shareit.user.mapper.UserMapper.toUserDto;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto addUser(UserDto userDto) {
        try {
            return toUserDto(userRepository.save(UserMapper.toUser(userDto)));
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistException("Пользователь с email = " + userDto.getEmail() + " уже существует.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsers() {
        return UserMapper.toListUsersDto(userRepository.findAll());
    }

    @Override
    public UserDto updateUserById(Integer id, UserDto userDto) {
        try {
            User updatedUser = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("Нет пользователя с id =  " + id));
            if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) {
                updatedUser.setEmail(userDto.getEmail());
            }
            if (userDto.getName() != null && !userDto.getName().isBlank()) {
                updatedUser.setName(userDto.getName());
            }
            return toUserDto(userRepository.saveAndFlush(updatedUser));
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistException("Пользователь с email = " + userDto.getEmail() + " уже существует.");
        }
    }

    @Override
    public void deleteUserById(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Нет пользователя с id =  " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserDto getUserById(Integer id) {
        return toUserDto(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Нет пользователя с id =  " + id)));
    }
}