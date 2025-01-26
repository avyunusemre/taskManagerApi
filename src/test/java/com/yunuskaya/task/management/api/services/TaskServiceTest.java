package com.yunuskaya.task.management.api.services;

import com.yunuskaya.task.management.api.dto.TaskDTO;
import com.yunuskaya.task.management.api.entities.Task;
import com.yunuskaya.task.management.api.entities.User;
import com.yunuskaya.task.management.api.enums.TaskStatus;
import com.yunuskaya.task.management.api.mappers.TaskMapper;
import com.yunuskaya.task.management.api.repositories.TaskRepository;
import com.yunuskaya.task.management.api.services.implementations.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;
    private TaskDTO taskDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus(TaskStatus.PENDING);
        task.setUser(user);

        taskDTO = TaskMapper.INSTANCE.toDTO(task);
        taskDTO.setUserId(user.getId());
    }

    @Test
    void testCreateTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        doNothing().when(messagingTemplate).convertAndSend(anyString(), any(TaskDTO.class));

        TaskDTO createdTask = taskService.createTask(taskDTO);

        assertNotNull(createdTask);
        assertEquals("Test Task", createdTask.getTitle());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testGetTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskDTO retrievedTask = taskService.getTask(1L);

        assertNotNull(retrievedTask);
        assertEquals("Test Task", retrievedTask.getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteTask() {
        when(taskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(1L);

        assertDoesNotThrow(() -> taskService.deleteTask(1L));

        verify(taskRepository, times(1)).existsById(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

}
