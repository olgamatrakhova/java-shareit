package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(ItemDto itemDto, Integer itemId);

    List<ItemDto> getAllItems(Integer userId);

    ItemDto getItemById(Integer itemId, Integer userId);

    ItemDto updateItemById(ItemDto itemDto, Integer userId, Integer itemId);

    ItemDto deleteItemById(Integer itemId);

    List<ItemDto> searchItem(String text);
}