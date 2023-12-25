package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.Add;
import ru.practicum.shareit.util.Update;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> addUser(@Validated(Add.class) @RequestBody UserDto userDto, BindingResult bindingResult) throws AlreadyExistException {
        if (bindingResult.hasErrors()) {
            log.error("Не удалось выполнить запрос создание пользователя: {} , email: {} не прошёл валидацию или не уникален", userDto, userDto.getEmail());
            return ResponseEntity.badRequest().body(userDto);
        }
        log.info("Добавление пользователя {} (addUser)", userDto);
        return new ResponseEntity<>(userService.addUser(userDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        log.info("Получение всех пользователей (getUsers)");
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer id) {
        log.info("Получение пользователя по id = {} (getUserById)", id);
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateUserById(@PathVariable Integer id, @Validated(Update.class) @RequestBody UserDto userDto) {
        log.info("Обновление пользователя {} (updateUserById) ", userDto);
        return new ResponseEntity<>(userService.updateUserById(id, userDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDto> deleteUserById(@PathVariable Integer id) {
        log.info("Удаление пользователя c id = {} (deleteUserById)", id);
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}