package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class Item {
    private Integer id;
    private String name;
    private String description;
    private Integer ownerId;
    private Boolean available;
    private ItemRequest request;
}