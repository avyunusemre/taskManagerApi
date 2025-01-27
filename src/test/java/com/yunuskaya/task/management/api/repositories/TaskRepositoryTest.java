package com.yunuskaya.task.management.api.repositories;

import com.yunuskaya.task.management.api.entities.Task;
import com.yunuskaya.task.management.api.entities.User;
import com.yunuskaya.task.management.api.enums.Role;
import com.yunuskaya.task.management.api.enums.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class TaskRepositoryTest {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testSaveAndFindByStatus() {
        User user = new User();
        user.setEmail("testuser@example.com");
        user.setUsername("testUser");
        user.setPassword("password");
        user.setRole(Role.USER);
        user = userRepository.save(user);

        Task task = new Task();
        task.setTitle("New Task");
        task.setDescription("New Description");
        task.setStatus(TaskStatus.PENDING);
        task.setUser(user);
        task = taskRepository.save(task);

        Assertions.assertNotNull(task.getId());
        Assertions.assertEquals(user.getId(), task.getUser().getId());
    }

    @Test
    void testDeleteTask() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password");
        user.setEmail("testuser@example.com");
        user.setRole(Role.USER);
        user = userRepository.save(user);

        Task task = new Task();
        task.setTitle("To be deleted");
        task.setDescription("Task to Delete");
        task.setStatus(TaskStatus.PENDING);
        task.setUser(user);
        task = taskRepository.save(task);

        taskRepository.deleteById(task.getId());

        Assertions.assertFalse(taskRepository.findById(task.getId()).isPresent());
    }
}
