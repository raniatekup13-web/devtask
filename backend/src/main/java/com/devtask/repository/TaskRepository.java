package com.devtask.repository;
import com.devtask.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findByAssignedToId(Long userId);
    List<Task> findByProjectId(Long projectId);
    long countByProjectId(Long projectId);
}
