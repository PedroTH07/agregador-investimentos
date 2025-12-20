package io.projects.agregadorinvestimentos.service;

import io.projects.agregadorinvestimentos.controller.CreateUserDto;
import io.projects.agregadorinvestimentos.entity.User;
import io.projects.agregadorinvestimentos.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public UUID createUser(CreateUserDto createUserDto) {
        var entity = new User(
                createUserDto.username(),
                createUserDto.email(),
                createUserDto.password(),
                Instant.now(),
                null);
        var savedUser = repository.save(entity);
        return savedUser.getUserId();
    }

    public Optional<User> getUserById(String id) {
        return repository.findById(UUID.fromString(id));
    }

    public List<User> listUsers() {
        return repository.findAll();
    }
}
