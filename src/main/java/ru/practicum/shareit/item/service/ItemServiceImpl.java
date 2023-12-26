package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.BookingNotAvailableException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItemDto;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItemResponseDto;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto addItem(ItemDto itemDto, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Нет пользователя с id = " + userId));
        Item item = ItemMapper.toItem(itemDto, user);
        return toItemDto(itemRepository.save(item));
    }

    @Override
    public List<ItemResponseDto> getAllItemsByUserId(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Нет пользователя с id = " + userId);
        }
        List<ItemResponseDto> itemResponseDtoList = new ArrayList<>();
        List<Item> items = itemRepository.getItemsByOwnerIdOrderByIdAsc(userId);
        for (Item item : items) {
            List<CommentResponseDto> comments = CommentMapper
                    .toListComment(commentRepository.getAllByItemIdOrderByCreatedDesc(item.getId()));
            itemResponseDtoList.add(toItemResponseDto(item,
                    bookingRepository.getFirstByItem_idAndEndBeforeOrderByEndDesc(item.getId(), LocalDateTime.now()),
                    bookingRepository.getFirstByItem_idAndStartAfterOrderByStartAsc(item.getId(), LocalDateTime.now()),
                    comments));
        }
        return itemResponseDtoList;
    }

    @Override
    public ItemResponseDto getItemByIdAndUserId(Integer itemId, Integer userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Нет вещи с id = " + itemId));
        List<CommentResponseDto> comments = CommentMapper.toListComment(commentRepository.getAllByItemIdOrderByCreatedDesc(itemId));
        ItemResponseDto itemResponseDto = toItemResponseDto(item, null, null, comments);
        if (!item.getOwner().getId().equals(userId)) {
            return itemResponseDto;
        }
        List<Booking> lastBooking = bookingRepository.getTop1BookingByItemIdAndEndIsBeforeAndBookingStatusIs(
                itemId, LocalDateTime.now(), BookingStatus.APPROVED, Sort.by(DESC, "end"));
        List<Booking> nextBooking = bookingRepository.getTop1BookingByItemIdAndEndIsAfterAndBookingStatusIs(
                itemId, LocalDateTime.now(), BookingStatus.APPROVED, Sort.by(Sort.Direction.ASC, "end"));
        if (lastBooking.isEmpty() && !nextBooking.isEmpty()) {
            itemResponseDto.setLastBooking(BookingMapper.toBookingResponseDto(nextBooking.get(0)));
            itemResponseDto.setNextBooking(null);
        } else if (!lastBooking.isEmpty() && !nextBooking.isEmpty()) {
            itemResponseDto.setLastBooking(BookingMapper.toBookingResponseDto(lastBooking.get(0)));
            itemResponseDto.setNextBooking(BookingMapper.toBookingResponseDto(nextBooking.get(0)));
        }
        return itemResponseDto;
    }

    @Override
    public ItemDto updateItemById(ItemDto itemDto, Integer userId, Integer itemId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Нет пользователя с id = " + userId);
        }
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Нет вещи с id = " + itemId));
        if (!item.getOwner().getId().equals(userId)) {
            throw new ItemNotFoundException("Нет вещи с id = " + itemId);
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return toItemDto(itemRepository.save(item));
    }

    @Override
    public void deleteItemById(Integer itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new ItemNotFoundException("Нет вещи с id = " + itemId);
        }
        itemRepository.deleteById(itemId);
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        return ItemMapper.toItemListDto(itemRepository.search(text));
    }

    @Override
    public CommentResponseDto createComment(Integer userId, Integer itemId, CommentRequestDto commentRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Нет пользователя с id = " + userId));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Нет вещи с id = " + itemId));
        List<Booking> userBookings = bookingRepository.getByItem_IdAndBooker_IdOrderByStartDesc(itemId, userId);
        if (!userBookings.isEmpty()) {
            if (userBookings.stream().anyMatch(booking ->
                    (booking.getBookingStatus() != BookingStatus.REJECTED)
                            && booking.getBookingStatus() != BookingStatus.WAITING
                            && booking.getEnd().isBefore(LocalDateTime.now()))) {
                Comment comment = Comment.builder()
                        .item(item)
                        .author(user)
                        .text(commentRequestDto.getText())
                        .build();
                return CommentMapper.toResponseDto(commentRepository.save(comment));
            } else {
                log.error("Не выполнен запрос на создание комментария");
                throw new BookingNotAvailableException("Вещь не забронирована. Невозможно оставить комментарий.");
            }
        } else {
            log.error("Не выполнен запрос на создание комментария");
            throw new BookingNotAvailableException("Пользователь не бронировал вещь");
        }
    }
}