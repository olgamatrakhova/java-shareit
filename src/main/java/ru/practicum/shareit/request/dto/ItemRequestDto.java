package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Builder
@Data
public class ItemRequestDto {
    private Integer id;
    private String description;
    private User requester;
    private LocalDateTime created;
}