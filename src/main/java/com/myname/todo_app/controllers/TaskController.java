package com.myname.todo_app.controllers;


import com.myname.todo_app.exception.TaskNotFoundException;
import com.myname.todo_app.model.Task;
import com.myname.todo_app.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/tasks")
@Api(value = "Task Management System", description = "Operations pertaining to task management in the application")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private MessageSource messageSource;

    @ApiOperation(value = "View a list of available tasks", response = List.class)
    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @ApiOperation(value = "Get a task by Id")
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        try {
            Task task = taskService.getTaskById(id);
            return ResponseEntity.ok(task);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @ApiOperation(value = "Create a new task")
    @PostMapping
    public Task createTask(@Valid @RequestBody Task task) {
        return taskService.createTask(task);
    }

    @ApiOperation(value = "Update an existing task")
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable long id, @Valid @RequestBody Task taskDetails) {
        final Task updatedTask = taskService.updateTask(id, taskDetails);
        return ResponseEntity.ok(updatedTask);
    }

    @ApiOperation(value = "Delete a task")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {

        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<Task>> getTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Page<Task> tasks = taskService.getTasks(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(tasks);
    }
    @GetMapping("/welcome")
    public String welcome(@RequestParam(value = "lang", required = false) String lang) {
        Locale locale = Locale.forLanguageTag((lang != null ? lang : "en"));
        return messageSource.getMessage("welcome.message", null, locale);
    }
}
