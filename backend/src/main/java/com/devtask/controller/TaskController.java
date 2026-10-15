package com.devtask.controller;
import com.devtask.dto.TaskRequest;
import com.devtask.model.Task;
import com.devtask.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.devtask.model.User;
import com.devtask.model.TaskStatus;
import com.devtask.repository.UserRepository;

@RestController @RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService service; private final UserRepository users;
    public TaskController(TaskService service,UserRepository users){this.service=service;this.users=users;}
    @GetMapping public Object all(@AuthenticationPrincipal String email){return service.all(users.findByEmail(email).orElseThrow());}
    @GetMapping("/{id}") public Object get(@PathVariable Long id,@AuthenticationPrincipal String email){return service.get(id,users.findByEmail(email).orElseThrow());}
    @PostMapping @PreAuthorize("hasRole('ADMIN')") public Object create(@Valid @RequestBody TaskRequest r){return service.create(r);}
    @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public Object update(@PathVariable Long id,@Valid @RequestBody TaskRequest r){return service.update(id,r);}
    @PatchMapping("/{id}/status") public Object status(@PathVariable Long id,@Valid @RequestBody StatusRequest r,@AuthenticationPrincipal String email){User actor=users.findByEmail(email).orElseThrow();return service.updateStatus(id,r.status(),actor);}
    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public void delete(@PathVariable Long id){service.delete(id);}
    public record StatusRequest(@jakarta.validation.constraints.NotNull TaskStatus status){}
}
