package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(ItemDto itemDto, Integer itemId);

    List<ItemResponseDto> getAllItemsByUserId(Integer userId);

    ItemResponseDto getItemByIdAndUserId(Integer itemId, Integer userId);

    ItemDto updateItemById(ItemDto itemDto, Integer userId, Integer itemId);

    void deleteItemById(Integer itemId);

    List<ItemDto> searchItem(String text);

    CommentResponseDto createComment(Integer userId, Integer itemId, CommentRequestDto commentRequestDto);
}