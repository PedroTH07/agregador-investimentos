package io.projects.agregadorinvestimentos.service;

import io.projects.agregadorinvestimentos.controller.dto.CreateAccountDto;
import io.projects.agregadorinvestimentos.controller.dto.CreateUserDto;
import io.projects.agregadorinvestimentos.controller.dto.ResponseAccountDto;
import io.projects.agregadorinvestimentos.controller.dto.UpdateUserDto;
import io.projects.agregadorinvestimentos.entity.Account;
import io.projects.agregadorinvestimentos.entity.BillingAddress;
import io.projects.agregadorinvestimentos.entity.User;
import io.projects.agregadorinvestimentos.repository.AccountRepository;
import io.projects.agregadorinvestimentos.repository.BillingAddressRepository;
import io.projects.agregadorinvestimentos.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final BillingAddressRepository billingAddressRepository;

    public UserService(UserRepository userRepository, AccountRepository accountRepository, BillingAddressRepository billingAddressRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.billingAddressRepository = billingAddressRepository;
    }

    public UUID createUser(CreateUserDto createUserDto) {
        var entity = new User(
                createUserDto.username(),
                createUserDto.email(),
                createUserDto.password(),
                Instant.now(),
                null);
        var savedUser = userRepository.save(entity);
        return savedUser.getUserId();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(UUID.fromString(id));
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public void updateUser(String userId, UpdateUserDto body) {
        var id = UUID.fromString(userId);
        var userEntity = userRepository.findById(id);
        if (userEntity.isEmpty()) return;

        var user = userEntity.get();

        if (body.password() != null) {
            user.setPassword(body.password());
        }
        if (body.username() != null) {
            user.setUsername(body.username());
        }

        userRepository.save(user);
    }

    public void deleteUserById(String userId) {
        var id = UUID.fromString(userId);
        var userExists = userRepository.existsById(id);

        if (userExists) userRepository.deleteById(id);
    }

    @Transactional
    public void createAccount(String userId, CreateAccountDto data) {
        var user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var account = new Account(
                null,
                data.description(),
                user,
                null,
                new ArrayList<>()
        );
        var accountCreated = accountRepository.save(account);

        var billingAddress = new BillingAddress(
                accountCreated.getAccountId(),
                accountCreated,
                data.street(),
                data.number()
        );
        billingAddressRepository.save(billingAddress);
    }

    public List<ResponseAccountDto> listAccounts(String userId) {
        var user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return user.getAccounts()
                .stream()
                .map(ac -> new ResponseAccountDto(
                        ac.getAccountId().toString(),
                        ac.getDescription()))
                .toList();
    }
}
