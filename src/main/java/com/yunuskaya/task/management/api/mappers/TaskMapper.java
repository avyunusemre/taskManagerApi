package com.yunuskaya.task.management.api.mappers;

import com.yunuskaya.task.management.api.dto.TaskDTO;
import com.yunuskaya.task.management.api.entities.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TaskMapper {
    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    @Mapping(source = "user.id", target = "userId")
    TaskDTO toDTO(Task task);

    @Mapping(source = "userId", target = "user.id")
    Task toEntity(TaskDTO taskDTO);
}
