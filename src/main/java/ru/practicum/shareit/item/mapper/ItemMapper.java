package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

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
                        .owner((i.getOwner()))
                        .available(i.getAvailable())
                        .requestId(i.getRequestId())
                        .build())
                .orElse(null);
    }

    public static Item toItem(ItemDto itemDto, User owner) {
        return Optional.ofNullable(itemDto)
                .map(dto ->
                        Item.builder()
                                .id(dto.getId())
                                .name(dto.getName())
                                .description(dto.getDescription())
                                .owner(owner)
                                .available(dto.getAvailable())
                                .requestId(dto.getRequestId())
                                .build())
                .orElse(null);
    }

    public static List<ItemDto> toItemListDto(List<Item> itemList) {
        return itemList.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    public static ItemResponseDto toItemResponseDto(Item item,
                                                    Booking lastBooking,
                                                    Booking nextBooking,
                                                    List<CommentResponseDto> comments) {
        return ItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .available(item.getAvailable())
                .description(item.getDescription())
                .lastBooking(lastBooking == null ? null : BookingMapper.toBookingResponseDto(lastBooking))
                .nextBooking(nextBooking == null ? null : BookingMapper.toBookingResponseDto(nextBooking))
                .comments(comments)
                .build();
    }
}