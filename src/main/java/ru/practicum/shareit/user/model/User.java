package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Builder
@Data
public class User {
    @Positive(message = "Идентификатор пользователя должен быть положительным")
    private Integer id;
    @NotBlank(message = "Имя не должно быть пустым")
    private String name;
    @Email
    @NotBlank(message = "Email не должен быть пустым")
    private String email;
}