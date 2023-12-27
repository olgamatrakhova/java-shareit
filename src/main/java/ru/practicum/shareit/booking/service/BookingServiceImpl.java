package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.BookingNotAvailableException;
import ru.practicum.shareit.exception.BookingNotFoundException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto add(BookingRequestDto bookingRequestDto, Integer userId) {
        Item item = itemRepository
                .findById(bookingRequestDto.getItemId())
                .orElseThrow(() -> new ItemNotFoundException("Нет вещи с id = " + bookingRequestDto.getItemId()));
        User booker = userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Нет пользователя с id = " + userId));
        if (!item.getAvailable()) {
            throw new BookingNotAvailableException("Вещь c id = " + bookingRequestDto.getItemId() + "не доступна для бронирования");
        }
        if (item.getOwner().getId().equals(userId)) {
            throw new UserNotFoundException("Нельзя забронировать свою вещь");
        }
        if (bookingRequestDto.getStart().isAfter(bookingRequestDto.getEnd())) {
            throw new BookingNotAvailableException("Не верное время бронирования. Дата начала не может быть позже даты окончания");
        }
        if (bookingRequestDto.getStart().equals(bookingRequestDto.getEnd())) {
            throw new BookingNotAvailableException("Не верное время бронирования. Дата начала не может быть равна дате окончания");
        }
        if (bookingRequestDto.getEnd().isBefore(LocalDateTime.now())) {
            throw new BookingNotAvailableException("Не верное время бронирования. Дата окончания не может быть раньше, чем текущая дата");
        }
        Booking booking = Booking.builder()
                .bookingStatus(BookingStatus.WAITING)
                .booker(booker)
                .item(item)
                .start(bookingRequestDto.getStart())
                .end(bookingRequestDto.getEnd())
                .build();
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto approve(Integer bookingId, Integer userId, Boolean approved) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Нет пользователя с id = " + userId);
        }
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new UserNotFoundException("Вещь не забронирована"));
        if (booking.getBookingStatus() != BookingStatus.WAITING) {
            throw new BookingNotAvailableException("Статус брони должен быть в ожидании - 'WAITING'");
        }
        if (booking.getBooker().getId().equals(userId)) {
            throw new UserNotFoundException("Подтвердить бронирование может только владелец вещи");
        }
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new BookingNotAvailableException("Подтвердить бронирование может только владелец вещи");
        }
        booking.setBookingStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public List<BookingDto> getAllByOwner(Integer ownerId, String state) {
        if (!userRepository.existsById(ownerId)) {
            throw new UserNotFoundException("Нет пользователя с id = " + ownerId);
        }
        List<Booking> bookings;
        switch (state.toUpperCase()) {
            case "ALL":
                bookings = bookingRepository.getByItemOwnerIdOrderByStartDesc(ownerId);
                break;
            case "CURRENT":
                bookings = bookingRepository.getByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(ownerId, LocalDateTime.now(), LocalDateTime.now());
                break;
            case "PAST":
                bookings = bookingRepository.getByItemOwnerIdAndEndIsBeforeOrderByStartDesc(ownerId, LocalDateTime.now());
                break;
            case "FUTURE":
                bookings = bookingRepository.getByItemOwnerIdAndStartIsAfterOrderByStartDesc(ownerId, LocalDateTime.now());
                break;
            case "WAITING":
                bookings = bookingRepository.getByItemOwnerIdAndBookingStatusOrderByStartDesc(ownerId, BookingStatus.WAITING);
                break;
            case "REJECTED":
                bookings = bookingRepository.getByItemOwnerIdAndBookingStatusOrderByStartDesc(ownerId, BookingStatus.REJECTED);
                break;
            default:
                throw new BookingNotAvailableException("Unknown state: UNSUPPORTED_STATUS");
        }
        return BookingMapper.toBookingDtoList(bookings);

    }

    @Override
    public List<BookingDto> getAllByUser(Integer userId, String state) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Нет пользователя с id = " + userId);
        }
        List<Booking> bookings;
        switch (state.toUpperCase()) {
            case "ALL":
                bookings = bookingRepository.getAllByBooker_IdOrderByStartDesc(userId);
                break;
            case "CURRENT":
                bookings = bookingRepository.getAllByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now());
                break;
            case "PAST":
                bookings = bookingRepository.getAllByBooker_IdAndEndIsBeforeOrderByStartDesc(userId, LocalDateTime.now());
                break;
            case "FUTURE":
                bookings = bookingRepository.getAllByBooker_IdAndStartIsAfterOrderByStartDesc(userId, LocalDateTime.now());
                break;
            case "WAITING":
                bookings = bookingRepository.getAllByBooker_IdAndBookingStatusOrderByStartDesc(userId, BookingStatus.WAITING);
                break;
            case "REJECTED":
                bookings = bookingRepository.getAllByBooker_IdAndBookingStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
                break;
            default:
                throw new BookingNotAvailableException("Unknown state: UNSUPPORTED_STATUS");
        }
        return BookingMapper.toBookingDtoList(bookings);
    }

    @Override
    public BookingDto getByIdAndBookerId(Integer bookingId, Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Нет пользователя с id = " + userId);
        }
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Вещь не забронирована"));
        Item item = itemRepository.findById(booking.getId())
                .orElseThrow(() -> new ItemNotFoundException("Нет такой вещи"));
        if (!item.getOwner().getId().equals(userId) && !booking.getBooker().getId().equals(userId)) {
            throw new UserNotFoundException("Нет пользователей с id = " + userId);
        }
        return BookingMapper.toBookingDto(booking);
    }
}