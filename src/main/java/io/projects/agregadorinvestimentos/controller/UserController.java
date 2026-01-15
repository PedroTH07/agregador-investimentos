package io.projects.agregadorinvestimentos.controller;

import io.projects.agregadorinvestimentos.controller.dto.CreateAccountDto;
import io.projects.agregadorinvestimentos.controller.dto.CreateUserDto;
import io.projects.agregadorinvestimentos.controller.dto.UpdateUserDto;
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

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable("userId") String userId,
                                           @RequestBody UpdateUserDto body) {
        service.updateUser(userId, body);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") String userId) {
        service.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/accounts")
    public ResponseEntity<Void> createAccount(@PathVariable("userId") String userId,
                                                  @RequestBody CreateAccountDto data) {
        service.createAccount(userId, data);
        return ResponseEntity.ok().build();
    }
}
