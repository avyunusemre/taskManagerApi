package com.yunuskaya.task.management.api.mappers;

import com.yunuskaya.task.management.api.dto.UserDTO;
import com.yunuskaya.task.management.api.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO toDTO(User user);
    User toEntity(UserDTO userDTO);
}
