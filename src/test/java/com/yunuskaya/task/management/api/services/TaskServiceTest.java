package com.yunuskaya.task.management.api.services;

import com.yunuskaya.task.management.api.dto.TaskDTO;
import com.yunuskaya.task.management.api.entities.Task;
import com.yunuskaya.task.management.api.entities.User;
import com.yunuskaya.task.management.api.enums.TaskStatus;
import com.yunuskaya.task.management.api.exceptions.TaskNotFoundException;
import com.yunuskaya.task.management.api.repositories.TaskRepository;
import com.yunuskaya.task.management.api.repositories.UserRepository;
import com.yunuskaya.task.management.api.services.implementations.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;
    private TaskDTO taskDTO;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus(TaskStatus.PENDING);
        task.setUser(user);

        taskDTO = new TaskDTO();
        taskDTO.setTitle("Test Task");
        taskDTO.setDescription("Test Description");
        taskDTO.setStatus(TaskStatus.PENDING);
        taskDTO.setUserId(user.getId());
    }


    @Test
    void testCreateTask() {
        when(userRepository.findById(taskDTO.getUserId())).thenReturn(Optional.of(user));

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDTO createdTask = taskService.createTask(taskDTO);

        assertNotNull(createdTask);
        assertEquals(taskDTO.getTitle(), createdTask.getTitle());
        assertEquals(taskDTO.getDescription(), createdTask.getDescription());

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testGetTaskById() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskDTO foundTask = taskService.getTask(1L);

        assertNotNull(foundTask);
        assertEquals(task.getTitle(), foundTask.getTitle());

        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTaskByIdNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTask(1L));
    }

    @Test
    void testUpdateTask() {
        TaskDTO updatedTaskDTO = new TaskDTO();
        updatedTaskDTO.setTitle("Updated Task");
        updatedTaskDTO.setDescription("Updated Description");
        updatedTaskDTO.setStatus(TaskStatus.COMPLETED);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDTO result = taskService.updateTask(task.getId(), updatedTaskDTO);

        assertNotNull(result);
        assertEquals("Updated Task", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(TaskStatus.COMPLETED, result.getStatus());

        verify(taskRepository, times(1)).findById(task.getId());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testDeleteTask() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).deleteById(task.getId());

        assertDoesNotThrow(() -> taskService.deleteTask(task.getId()));

        verify(taskRepository, times(1)).findById(task.getId());
        verify(taskRepository, times(1)).deleteById(task.getId());
    }

    @Test
    void testDeleteTaskNotFound() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.deleteTask(task.getId()));

        verify(taskRepository, times(1)).findById(task.getId());
        verify(taskRepository, never()).deleteById(anyLong());
    }

    @Test
    void testGetTasksByStatus() {
        when(taskRepository.findByStatus(TaskStatus.PENDING)).thenReturn(List.of(task));

        List<TaskDTO> tasks = taskService.getTasksByStatus(TaskStatus.PENDING);

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals(TaskStatus.PENDING, tasks.get(0).getStatus());

        verify(taskRepository, times(1)).findByStatus(TaskStatus.PENDING);
    }
}
