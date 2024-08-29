package com.myname.todo_app.repository;

import com.myname.todo_app.model.Task;
import com.myname.todo_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user);
}
