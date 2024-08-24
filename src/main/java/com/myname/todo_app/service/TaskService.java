package com.myname.todo_app.service;

import com.myname.todo_app.model.Task;
import com.myname.todo_app.repository.TaskRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Data
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }
    public Task updateTask(Long id, Task detailTask) {
        Task task = taskRepository.findById(id).orElseThrow(()-> new RuntimeException("Task not found"));
        task.setDescription(detailTask.getDescription());
        task.setCompleted(detailTask.isCompleted());
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
