package com.example.taskhub.service;

import com.example.taskhub.dto.TaskForm;
import com.example.taskhub.model.Task;
import com.example.taskhub.model.TaskStatus;
import com.example.taskhub.model.User;
import com.example.taskhub.repository.TaskRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class TaskService {

    private final TaskRepository tasks;

    public TaskService(TaskRepository tasks) {
        this.tasks = tasks;
    }

    @Transactional(readOnly = true)
    public List<Task> listFor(User owner) {
        return tasks.findByOwnerOrderByIdDesc(owner);
    }

    @Transactional(readOnly = true)
    public Task getOwned(Long id, User owner) {
        return tasks.findByIdAndOwner(id, owner)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    public void save(TaskForm form, User owner) {
        Task task = form.getId() == null ? new Task() : getOwned(form.getId(), owner);
        task.setOwner(owner);
        task.setTitle(form.getTitle().trim());
        task.setDescription(form.getDescription());
        task.setStatus(form.getStatus());
        task.setDueDate(form.getDueDate());
        tasks.save(task);
    }

    public void changeStatus(Long id, TaskStatus status, User owner) {
        Task task = getOwned(id, owner);
        task.setStatus(status);
        tasks.save(task);
    }

    public void delete(Long id, User owner) {
        tasks.delete(getOwned(id, owner));
    }
}
