package com.example.scheduletracker.mapper;

import com.example.scheduletracker.dto.UserDto;
import com.example.scheduletracker.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserDto toDto(User user);
  User toEntity(UserDto dto);
}
