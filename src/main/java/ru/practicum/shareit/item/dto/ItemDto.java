package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
public class ItemDto {
    @Positive(message = "Идентификатор вещи должен быть положительным")
    private Integer id;
    @NotBlank(message = "Наименование вещи не должно быть пустым")
    private String name;
    @NotBlank
    @Size(max = 300, message = "Описание не должно превышать 300 символов")
    private String description;
    private User owner;
    @NotNull
    private Boolean available;
    private ItemRequest request;
    private Integer requestId;
    private BookingResponseDto lastBooking;
    private BookingResponseDto nextBooking;
    private List<CommentResponseDto> comments;
}