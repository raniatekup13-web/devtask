package com.devtask.service;

import com.devtask.dto.TaskResponse;
import com.devtask.model.Project;
import com.devtask.model.Role;
import com.devtask.model.Task;
import com.devtask.model.TaskStatus;
import com.devtask.model.User;
import com.devtask.repository.ProjectRepository;
import com.devtask.repository.TaskRepository;
import com.devtask.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;

import java.lang.reflect.Proxy;
import java.util.Optional;
import java.lang.reflect.InvocationHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
class TaskServiceAuthorizationTests {
    @Test
    void developerCanChangeStatusOfAssignedTask() {
        User developer = user(7L, Role.DEVELOPER);
        Task task = task(42L, developer);
        TaskService service = serviceFor(task);

        TaskResponse result = service.updateStatus(42L, TaskStatus.IN_PROGRESS, developer);

        assertEquals(TaskStatus.IN_PROGRESS, result.status());
    }

    @Test
    void developerCannotChangeStatusOfAnotherDevelopersTask() {
        User actor = user(7L, Role.DEVELOPER);
        User assignee = user(8L, Role.DEVELOPER);
        TaskService service = serviceFor(task(42L, assignee));

        assertThrows(AccessDeniedException.class,
                () -> service.updateStatus(42L, TaskStatus.DONE, actor));
    }

    private static User user(Long id, Role role) {
        User user = new User();
        user.setId(id);
        user.setEmail("member" + id + "@example.com");
        user.setRole(role);
        return user;
    }

    private static TaskService serviceFor(Task task) {
        TaskRepository tasks = fake(TaskRepository.class, (proxy, method, args) -> switch (method.getName()) {
            case "findById" -> Optional.of(task);
            case "save" -> args[0];
            default -> null;
        });
        ProjectRepository projects = fake(ProjectRepository.class, (proxy, method, args) -> null);
        UserRepository users = fake(UserRepository.class, (proxy, method, args) -> null);
        return new TaskService(tasks, projects, users);
    }

    private static <T> T fake(Class<T> type, InvocationHandler handler) {
        return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, handler));
    }

    private static Task task(Long id, User assignee) {
        Project project = new Project();
        project.setId(3L);
        project.setName("Project");
        Task task = new Task();
        task.setId(id);
        task.setTitle("Task");
        task.setProject(project);
        task.setAssignedTo(assignee);
        task.setStatus(TaskStatus.TODO);
        return task;
    }
}
