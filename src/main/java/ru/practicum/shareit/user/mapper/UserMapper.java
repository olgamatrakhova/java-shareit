package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class UserMapper {
    private UserMapper() {
    }

    public static UserDto toUserDto(User user) {
        return Optional.ofNullable(user)
                .map(u -> UserDto.builder()
                        .id(u.getId())
                        .name(u.getName())
                        .email(u.getEmail())
                        .build())
                .orElse(null);

    }

    public static User toUser(UserDto userDto) {
        return Optional.ofNullable(userDto)
                .map(dto -> User.builder()
                        .id(dto.getId())
                        .name(dto.getName())
                        .email(dto.getEmail())
                        .build())
                .orElse(null);
    }

    public static List<UserDto> toListUsersDto(Collection<User> users) {
        List<UserDto> usersDto = new ArrayList<>();
        for (User user : users) {
            usersDto.add(toUserDto(user));
        }
        return usersDto;
    }
}