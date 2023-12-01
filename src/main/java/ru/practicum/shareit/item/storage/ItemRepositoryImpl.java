package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Integer, Item> itemsMap = new HashMap<>();
    private Integer idItem = 1;

    @Override
    public Item addItem(Item item, Integer userId) {
        item.setId(idItem);
        item.setOwnerId(userId);
        log.info("Добавлена вещь с id = {}, владелец с id = {} (createItem({},{}))", item, userId, item, userId);
        itemsMap.put(idItem++, item);
        return item;
    }

    @Override
    public List<Item> getAllItems(Integer userId) {
        log.info("Получение списка всех вещей одного владельца c id = {} (getAllItems({}))", userId, userId);
        return itemsMap.values().stream().filter(item -> Objects.equals(item.getOwnerId(), userId)).collect(Collectors.toList());
    }

    @Override
    public Item getItemById(Integer itemId, Integer userId) {
        if (itemsMap.containsKey(itemId)) {
            log.info("Получение вещи с id = {} (getItemById({},{}))", itemId, itemId, userId);
            return itemsMap.get(itemId);
        }
        log.warn("Получение вещи с id = {}, вещь не найдена (getItemById({},{}))", itemId, itemId, userId);
        throw new ItemNotFoundException("Нет такой вещи");
    }

    @Override
    public Item updateItemById(Item item, Integer userId, Integer itemId) {
        Item oldItem = itemsMap.get(itemId);
        if (!itemsMap.containsKey(itemId)) {
            log.warn("Обновление вещи с id = {}, вещь не найдена (updateItemById({},{},{}))", itemId, item, userId, itemId);
            throw new ItemNotFoundException("Нет такой вещи");
        }
        if (!Objects.equals(oldItem.getOwnerId(), userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не верно указан владелец");
        }
        if (item.getName() != null && !item.getName().isBlank()) {
            oldItem.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            oldItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            oldItem.setAvailable(item.getAvailable());
        }
        log.info("Обновление вещи {} (updateItemById({},{},{}))", oldItem, item, userId, itemId);
        return oldItem;
    }

    @Override
    public Item deleteItemById(Integer id) {
        if (itemsMap.containsKey(id)) {
            log.info("Удаление вещи с id = {} (deleteItemById({}))", id, id);
            return itemsMap.remove(id);
        }
        log.warn("Удаление вещи с id = {}, но такой вещи нет (deleteItemById({}))", id, id);
        throw new ItemNotFoundException("Нет такой вещи");
    }

    @Override
    public List<Item> searchItem(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        return itemsMap.values().stream().filter(Item::getAvailable).filter(item -> item.getDescription().toLowerCase().contains(text.toLowerCase()) || item.getName().toLowerCase().contains(text.toLowerCase())).collect(Collectors.toList());
    }
}