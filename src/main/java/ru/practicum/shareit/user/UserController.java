package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> addUser(@Valid @RequestBody UserDto userDto) {
        log.info("Добавление пользователя {} (addUser({}))", userDto, userDto);
        return new ResponseEntity<>(userService.addUser(userDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        log.info("Получение всех пользователей (getUsers())");
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer id) {
        log.info("Получение пользователя по id = {} (getUserById({}))", id, id);
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateUserById(@PathVariable Integer id, @RequestBody UserDto userDto) {
        log.info("Обновление пользователя {} (updateUserById({},{}))", userDto, id, userDto);
        return new ResponseEntity<>(userService.updateUserById(id, userDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDto> deleteUserById(@PathVariable Integer id) {
        log.info("Удаление пользователя c id = {} (deleteUserById({}))", id, id);
        return new ResponseEntity<>(userService.deleteUserById(id), HttpStatus.OK);
    }
}