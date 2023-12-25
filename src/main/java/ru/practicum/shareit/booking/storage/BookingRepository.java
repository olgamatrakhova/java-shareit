package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> getAllByBooker_IdOrderByStartDesc(Integer bookerId);

    List<Booking> getAllByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Integer bookerId, LocalDateTime start, LocalDateTime end);

    List<Booking> getAllByBooker_IdAndEndIsBeforeOrderByStartDesc(Integer bookerId, LocalDateTime end);

    List<Booking> getAllByBooker_IdAndStartIsAfterOrderByStartDesc(Integer bookerId, LocalDateTime start);

    List<Booking> getAllByBooker_IdAndBookingStatusOrderByStartDesc(Integer bookerId, BookingStatus bookingStatus);

    List<Booking> getByItemOwnerIdOrderByStartDesc(Integer ownerId);

    List<Booking> getByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Integer ownerId, LocalDateTime start, LocalDateTime end);

    List<Booking> getByItemOwnerIdAndEndIsBeforeOrderByStartDesc(Integer ownerId, LocalDateTime end);

    List<Booking> getByItemOwnerIdAndStartIsAfterOrderByStartDesc(Integer ownerId, LocalDateTime start);

    List<Booking> getByItemOwnerIdAndBookingStatusOrderByStartDesc(Integer ownerId, BookingStatus bookingStatus);

    List<Booking> getByItem_IdAndBooker_IdOrderByStartDesc(Integer itemId, Integer userId);

    List<Booking> getTop1BookingByItemIdAndEndIsBeforeAndBookingStatusIs(Integer itemId, LocalDateTime now, BookingStatus approved, Sort end);

    List<Booking> getTop1BookingByItemIdAndEndIsAfterAndBookingStatusIs(Integer itemId, LocalDateTime now, BookingStatus approved, Sort end);

    Booking getFirstByItem_idAndEndBeforeOrderByEndDesc(Integer itemId, LocalDateTime now);

    Booking getFirstByItem_idAndStartAfterOrderByStartAsc(Integer itemId, LocalDateTime now);
}