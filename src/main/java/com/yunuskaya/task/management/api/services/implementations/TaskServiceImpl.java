package com.yunuskaya.task.management.api.services.implementations;


import com.yunuskaya.task.management.api.dto.TaskDTO;
import com.yunuskaya.task.management.api.entities.Task;
import com.yunuskaya.task.management.api.entities.User;
import com.yunuskaya.task.management.api.enums.TaskStatus;
import com.yunuskaya.task.management.api.exceptions.TaskNotFoundException;
import com.yunuskaya.task.management.api.mappers.TaskMapper;
import com.yunuskaya.task.management.api.repositories.TaskRepository;
import com.yunuskaya.task.management.api.repositories.UserRepository;
import com.yunuskaya.task.management.api.services.TaskService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository, SimpMessagingTemplate messagingTemplate) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @RateLimiter(name = "task-api", fallbackMethod = "rateLimitFallback")
    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        User user = userRepository.findById(taskDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Task task = TaskMapper.INSTANCE.toEntity(taskDTO);
        task.setUser(user);

        Task savedTask = taskRepository.save(task);

        messagingTemplate.convertAndSend("/user/" + task.getUser().getId() + "/tasks", savedTask);

        return TaskMapper.INSTANCE.toDTO(savedTask);
    }

    @RateLimiter(name = "task-api", fallbackMethod = "rateLimitFallback")
    @Override
    public TaskDTO getTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + id + " not found"));
        return TaskMapper.INSTANCE.toDTO(task);
    }

    @RateLimiter(name = "task-api", fallbackMethod = "rateLimitFallback")
    @Override
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        existingTask.setTitle(Optional.ofNullable(taskDTO.getTitle()).orElse(existingTask.getTitle()));
        existingTask.setDescription(Optional.ofNullable(taskDTO.getDescription()).orElse(existingTask.getDescription()));
        existingTask.setStatus(Optional.ofNullable(taskDTO.getStatus()).orElse(existingTask.getStatus()));

        if (taskDTO.getUserId() != null) {
            User user = userRepository.findById(taskDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            existingTask.setUser(user);
        }

        Task updatedTask = taskRepository.save(existingTask);
        TaskDTO updatedTaskDTO = TaskMapper.INSTANCE.toDTO(updatedTask);

        String userDestination = "/user/" + updatedTask.getUser().getUsername() + "/task-updates";
        messagingTemplate.convertAndSend(userDestination, updatedTaskDTO);

        return TaskMapper.INSTANCE.toDTO(updatedTask);
    }

    @RateLimiter(name = "task-api", fallbackMethod = "rateLimitFallback")
    @Override
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        taskRepository.deleteById(id);
        messagingTemplate.convertAndSend("/user/" + task.getUser().getId() + "/tasks", "Task with ID " + id + " was deleted");
    }

    @RateLimiter(name = "task-api", fallbackMethod = "rateLimitFallback")
    @Override
    public List<TaskDTO> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status).stream()
                .map(TaskMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    public List<TaskDTO> rateLimitFallback(TaskStatus status, RuntimeException ex) {
        throw new RuntimeException("Too many requests - Please try again later");
    }
}
