package com.myname.todo_app.service;

import com.myname.todo_app.model.Task;
import com.myname.todo_app.repository.TaskRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Data
@AllArgsConstructor
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;

    public Page<Task> getTasks(int page, int size, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return taskRepository.findAll(pageable);
    }

    public List<Task> getAllTasks() {
        logger.info("Fetching all tasks");
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        logger.info("Fetching task by id: {}", id);
        return taskRepository.findById(id);
    }
    public Task createTask(Task task) {
        logger.info("Creating a new task: {}", task.getDescription());
        return taskRepository.save(task);
    }
    public Task updateTask(Long id, Task detailTask) {
        logger.info("Updating task with id: {}", id);
        Task task = taskRepository.findById(id).orElseThrow(()-> new RuntimeException("Task not found"));
        task.setDescription(detailTask.getDescription());
        task.setCompleted(detailTask.isCompleted());
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        logger.info("Deleting task with id: {}", id);
        taskRepository.deleteById(id);
    }
}
