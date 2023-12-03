package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item addItem(Item item, Integer id);

    List<Item> getAllItems(Integer userId);

    Item getItemById(Integer itemId, Integer userId);

    Item updateItemById(Item item, Integer userId, Integer itemId);

    Item deleteItemById(Integer id);

    List<Item> searchItem(String text);
}