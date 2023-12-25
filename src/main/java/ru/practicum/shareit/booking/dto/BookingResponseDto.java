package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.status.BookingStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingResponseDto {
    private Integer id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Integer itemId;
    private Integer bookerId;
    private BookingStatus bookingStatus;
}