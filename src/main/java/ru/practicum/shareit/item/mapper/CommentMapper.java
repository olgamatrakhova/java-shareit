package ru.practicum.shareit.item.mapper;

import org.jetbrains.annotations.NotNull;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CommentMapper {

    public static CommentResponseDto toResponseDto(Comment comment) {
        return Optional.ofNullable(comment)
                .map(c -> CommentResponseDto.builder()
                        .id(c.getId())
                        .text(c.getText())
                        .authorName(c.getAuthor().getName())
                        .created(c.getCreated())
                        .build())
                .orElse(null);
    }

    public static List<CommentResponseDto> toListComment(@NotNull List<Comment> comments) {
        return comments.stream().map(CommentMapper::toResponseDto).collect(Collectors.toList());
    }
}