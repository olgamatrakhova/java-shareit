package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ItemMapper {

    private ItemMapper() {
    }

    public static ItemDto toItemDto(Item item) {
        return Optional.ofNullable(item)
                .map(i -> ItemDto.builder()
                        .id(i.getId())
                        .name(i.getName())
                        .description(i.getDescription())
                        .ownerId((i.getOwnerId()))
                        .available(i.getAvailable())
                        .request(i.getRequest())
                        .build())
                .orElse(null);
    }

    public static Item toItem(ItemDto itemDto) {
        return Optional.ofNullable(itemDto)
                .map(dto ->
                        Item.builder()
                                .id(dto.getId())
                                .name(dto.getName())
                                .description(dto.getDescription())
                                .ownerId(dto.getOwnerId())
                                .available(dto.getAvailable())
                                .request(dto.getRequest())
                                .build())
                .orElse(null);
    }

    public static List<ItemDto> toItemListDto(List<Item> itemList) {
        return itemList.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }
}