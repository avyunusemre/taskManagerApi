package com.yunuskaya.task.management.api.repositories;

import com.yunuskaya.task.management.api.entities.Task;
import com.yunuskaya.task.management.api.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(TaskStatus status);
}
