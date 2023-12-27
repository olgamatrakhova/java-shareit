package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<ItemDto> addItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Integer id) {
        log.info("Добавление вещи : {} (addItem)", itemDto);
        return new ResponseEntity<>(itemService.addItem(itemDto, id), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ItemResponseDto>> getAllItems(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Получение вещей по владельцу {} (getAllItems)", userId);
        return new ResponseEntity<>(itemService.getAllItemsByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponseDto> getItemById(@PathVariable Integer itemId, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Получение вещи по id = {} (getItemById)", itemId);
        return new ResponseEntity<>(itemService.getItemByIdAndUserId(itemId, userId), HttpStatus.OK);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItemById(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Integer userId, @PathVariable Integer itemId) {
        log.info("Обновление вещи {} (updateItemById)", itemDto);
        return new ResponseEntity<>(itemService.updateItemById(itemDto, userId, itemId), HttpStatus.OK);
    }

    @DeleteMapping("/itemId")
    public ResponseEntity<ItemDto> deleteItemById(Integer id) {
        log.info("Удаление вещи {} (deleteItemById)", id);
        itemService.deleteItemById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> searchItem(@RequestParam String text) {
        return new ResponseEntity<>(itemService.searchItem(text), HttpStatus.OK);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Integer itemId, @RequestHeader("X-Sharer-User-Id") Integer userId,
                                                            @Valid @RequestBody CommentRequestDto commentRequestDto) {
        return new ResponseEntity<>(itemService.createComment(userId, itemId, commentRequestDto), HttpStatus.OK);
    }
}