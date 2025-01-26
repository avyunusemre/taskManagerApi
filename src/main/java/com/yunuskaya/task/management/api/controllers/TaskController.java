package com.yunuskaya.task.management.api.controllers;


import com.yunuskaya.task.management.api.dto.TaskDTO;
import com.yunuskaya.task.management.api.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.yunuskaya.task.management.api.enums.TaskStatus;

@RestController
@RequestMapping("api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Create New Task", description = "It creates a new task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO) {
        return ResponseEntity.ok(taskService.createTask(taskDTO));
    }

    @Operation(summary = "Get The Task", description = "It returns a task with specified by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The task returned successfully"),
            @ApiResponse(responseCode = "404", description = "No task found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTask(id));
    }

    @Operation(summary = "Update The Task", description = "It updates an existing task.")
    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        return ResponseEntity.ok(taskService.updateTask(id, taskDTO));
    }

    @Operation(summary = "Delete the task (Only ADMIN)", description = "Only ADMIN can delete the task.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get all tasks",
            description = "It returns all tasks. Optionally, filtering can be done with the 'status' parameter."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All tasks returned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid filter parameter"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
    })
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getTasks(@RequestParam(required = false) TaskStatus status) {
        return ResponseEntity.ok(taskService.getTasksByStatus(status));
    }
}
