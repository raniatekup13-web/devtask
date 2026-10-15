package com.devtask.controller;

import com.devtask.dto.UserResponse;
import com.devtask.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    private final UserRepository users;
    public UserController(UserRepository users){this.users=users;}
    @GetMapping public List<UserResponse> all(){return users.findAll().stream().map(u->new UserResponse(u.getId(),u.getEmail(),u.getRole().name())).toList();}
}
