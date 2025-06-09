package com.example.scheduletracker.mapper;

import com.example.scheduletracker.dto.UserDto;
import com.example.scheduletracker.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserDto toDto(User user);

  @Mapping(target = "password", ignore = true)
  @Mapping(target = "twoFaSecret", ignore = true)
  @Mapping(target = "twoFaEnabled", ignore = true)
  @Mapping(target = "active", ignore = true)
  User toEntity(UserDto dto);
}
