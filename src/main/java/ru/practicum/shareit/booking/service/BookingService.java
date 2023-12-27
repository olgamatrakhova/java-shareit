package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.util.List;

public interface BookingService {
    BookingDto add(BookingRequestDto bookingRequestDto, Integer userId);

    BookingDto approve(Integer bookingId, Integer userId, Boolean approved);

    List<BookingDto> getAllByOwner(Integer ownerId, String state);

    List<BookingDto> getAllByUser(Integer userId, String state);

    BookingDto getByIdAndBookerId(Integer bookingId, Integer userId);
}