package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, Integer userId) {
         userRepository.getUserById(userId);
        if (itemDto.getAvailable() == null || !itemDto.getAvailable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Нельзя создать вещь");
        }
        return ItemMapper.toItemDto(itemRepository.addItem(ItemMapper.toItem(itemDto), userId));
    }

    @Override
    public List<ItemDto> getAllItems(Integer userId) {
        return ItemMapper.toItemListDto(itemRepository.getAllItems(userId));
    }

    @Override
    public ItemDto getItemById(Integer itemId, Integer userId) {
        return ItemMapper.toItemDto(itemRepository.getItemById(itemId, userId));
    }

    @Override
    public ItemDto updateItemById(ItemDto itemDto, Integer userId, Integer itemId) {
        return ItemMapper.toItemDto(itemRepository.updateItemById(ItemMapper.toItem(itemDto), userId, itemId));
    }

    @Override
    public ItemDto deleteItemById(Integer id) {
        return ItemMapper.toItemDto(itemRepository.deleteItemById(id));
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        return ItemMapper.toItemListDto(itemRepository.searchItem(text));
    }
}