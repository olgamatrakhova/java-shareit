package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.util.Add;
import ru.practicum.shareit.util.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Data
@Builder
public class UserDto {
    @Positive(groups = {Add.class, Update.class}, message = "Идентификатор пользователя должен быть положительным")
    private Integer id;
    @NotBlank(groups = Add.class, message = "Имя не должно быть пустым")
    private String name;
    @Email(groups = {Add.class, Update.class}, regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    @NotEmpty(groups = Add.class, message = "Email не должен быть пустым")
    private String email;
}