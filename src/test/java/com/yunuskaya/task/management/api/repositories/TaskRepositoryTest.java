package com.yunuskaya.task.management.api.repositories;

import com.yunuskaya.task.management.api.entities.Task;
import com.yunuskaya.task.management.api.enums.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TaskRepositoryTest {
    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    void testSaveAndFindByStatus() {
        Task task = new Task();
        task.setTitle("New Task");
        task.setDescription("New Description");
        task.setStatus(TaskStatus.PENDING);
        taskRepository.save(task);

        List<Task> tasks = taskRepository.findByStatus(TaskStatus.PENDING);
        assertFalse(tasks.isEmpty());
        assertEquals("New Task", tasks.get(0).getTitle());
    }

    @Test
    void testDeleteTask() {
        Task task = new Task();
        task.setTitle("Task to Delete");
        task.setDescription("To be deleted");
        task.setStatus(TaskStatus.PENDING);
        task = taskRepository.save(task);

        taskRepository.deleteById(task.getId());

        assertFalse(taskRepository.findById(task.getId()).isPresent());
    }
}
