package com.myname.todo_app.service;

import com.myname.todo_app.exception.TaskNotFoundException;
import com.myname.todo_app.model.Task;
import com.myname.todo_app.model.User;
import com.myname.todo_app.repository.TaskRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@AllArgsConstructor
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public List<Task> getAllTasks() {
        User currentUser = userService.getCurrentUser();
        logger.info("Fetching all tasks for user: {}", currentUser.getUsername());
        return taskRepository.findByUser(currentUser);
    }

    public Task getTaskById(Long id) {
        logger.info("Fetching task by id: {}", id);
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task createTask(Task task) {
        User currentUser = userService.getCurrentUser();
        task.setUser(currentUser);
        logger.info("Creating a new task for user: {}", currentUser.getUsername());
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task taskDetails) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        User currentUser = userService.getCurrentUser();
        if (!task.getUser().equals(currentUser)) {
            logger.warn("Unauthorized attempt to update task with id: {}", id);
            throw new IllegalArgumentException("Unauthorized");
        }
        task.setDescription(taskDetails.getDescription());
        task.setCompleted(taskDetails.isCompleted());
        logger.info("Updating task with id: {}", id);
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        User currentUser = userService.getCurrentUser();
        if (!task.getUser().equals(currentUser)) {
            logger.warn("Unauthorized attempt to delete task with id: {}", id);
            throw new IllegalArgumentException("Unauthorized");
        }
        logger.info("Deleting task with id: {}", id);
        taskRepository.delete(task);
    }

    public Page<Task> getTasks(int page, int size, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        logger.info("Fetching tasks with pagination. Page: {}, Size: {}, SortBy: {}, Direction: {}", page, size, sortBy, sortDirection);
        return taskRepository.findAll(pageable);
    }
}

