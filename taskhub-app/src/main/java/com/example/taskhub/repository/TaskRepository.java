package com.example.taskhub.repository;

import com.example.taskhub.model.Task;
import com.example.taskhub.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByOwnerOrderByIdDesc(User owner);
    Optional<Task> findByIdAndOwner(Long id, User owner);
}
