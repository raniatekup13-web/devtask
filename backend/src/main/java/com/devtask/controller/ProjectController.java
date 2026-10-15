package com.devtask.controller;
import com.devtask.dto.ProjectRequest;
import com.devtask.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.devtask.repository.UserRepository;
import com.devtask.model.User;

@RestController @RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService service; private final UserRepository users;
    public ProjectController(ProjectService service, UserRepository users){this.service=service;this.users=users;}
    @GetMapping public Object all(){return service.all();}
    @GetMapping("/{id}") public Object get(@PathVariable Long id){return service.get(id);}
    @PostMapping @PreAuthorize("hasRole('ADMIN')") public Object create(@Valid @RequestBody ProjectRequest r,@AuthenticationPrincipal String email){User owner=users.findByEmail(email).orElseThrow();return service.create(r,owner);}
    @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public Object update(@PathVariable Long id,@Valid @RequestBody ProjectRequest r){return service.update(id,r);}
    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public void delete(@PathVariable Long id){service.delete(id);}
}
