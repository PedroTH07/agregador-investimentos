package io.projects.agregadorinvestimentos.controller;

import io.projects.agregadorinvestimentos.entity.User;
import io.projects.agregadorinvestimentos.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody CreateUserDto body) {
        var userId = service.createUser(body);
        return ResponseEntity.created(URI.create("/v1/users/" + userId.toString())).build();
    }

    @GetMapping("{userId}")
    public ResponseEntity<User> getById(@PathVariable("userId") String userId) {
        var user = service.getUserById(userId);
        if (user.isPresent()) return ResponseEntity.ok(user.get());

        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<User>> listUsers() {
        var users = service.listUsers();
        return ResponseEntity.ok(users);
    }
}
