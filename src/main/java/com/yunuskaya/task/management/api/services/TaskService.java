package com.yunuskaya.task.management.api.services;

import com.yunuskaya.task.management.api.dto.TaskDTO;
import com.yunuskaya.task.management.api.enums.TaskStatus;

import java.util.List;

public interface TaskService {
    TaskDTO createTask(TaskDTO taskDTO);
    TaskDTO getTask(Long id);
    TaskDTO updateTask(Long id, TaskDTO taskDTO);
    void deleteTask(Long id);
    List<TaskDTO> getTasksByStatus(TaskStatus status);
}
