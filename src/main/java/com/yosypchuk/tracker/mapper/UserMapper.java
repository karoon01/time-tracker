package com.yosypchuk.tracker.mapper;

import com.yosypchuk.tracker.model.DTO.UserDTO;
import com.yosypchuk.tracker.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User mapUser(UserDTO userDTO);

    UserDTO mapUserDto(User user);
}
