package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Builder
@Data
public class ItemRequestDto {
    @Positive
    private Integer id;
    @NotBlank(message = "Описание не должно быть пустым")
    private String description;
    private User requester;
    @DateTimeFormat
    private LocalDateTime created;
}