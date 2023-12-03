package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */
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
    private Integer ownerId;
    private Boolean available;
    private ItemRequest request;
}