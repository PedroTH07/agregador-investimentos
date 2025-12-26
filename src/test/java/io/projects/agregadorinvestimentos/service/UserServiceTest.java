package io.projects.agregadorinvestimentos.service;

import io.projects.agregadorinvestimentos.controller.CreateUserDto;
import io.projects.agregadorinvestimentos.controller.UpdateUserDto;
import io.projects.agregadorinvestimentos.entity.User;
import io.projects.agregadorinvestimentos.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Nested
    class createUser {

        @Test
        @DisplayName("should create a user with success")
        void shouldCreateUserWithSuccess() {
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "password",
                    Instant.now(),
                    null);
            doReturn(user).when(repository).save(userArgumentCaptor.capture());

            var input = new CreateUserDto(
                    "username",
                    "email@email.com",
                    "password");
            var output = service.createUser(input);

            assertNotNull(output);

            var userCaptured = userArgumentCaptor.getValue();
            assertEquals(input.username(), userCaptured.getUsername());
            assertEquals(input.email(), userCaptured.getEmail());
            assertEquals(input.password(), userCaptured.getPassword());
        }

        @Test
        @DisplayName(" should throw exception when error occurs")
        void shouldThrowExceptionWhenErrorOccurs() {

            doThrow(new RuntimeException()).when(repository).save(any());
            var input = new CreateUserDto(
                    "username",
                    "email@email.com",
                    "password");

            assertThrows(RuntimeException.class, () -> service.createUser(input));
        }
    }

    @Nested
    class getUserById {

        @Test
        @DisplayName("Should get user by id with success when optional is present")
        void shouldGetUserByIdWhenOptionalIsPresent() {

            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "password",
                    Instant.now(),
                    null);
            doReturn(Optional.of(user))
                    .when(repository)
                    .findById(uuidArgumentCaptor.capture());

            var output = service.getUserById(user.getUserId().toString());

            assertTrue(output.isPresent());
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("Should get user by id with success when optional is empty")
        void shouldGetUserByIdWhenOptionalIsEmpty() {

            var userId = UUID.randomUUID();
            doReturn(Optional.empty())
                    .when(repository)
                    .findById(uuidArgumentCaptor.capture());

            var output = service.getUserById(userId.toString());

            assertTrue(output.isEmpty());
            assertEquals(userId, uuidArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("Should throw exception when error occurs")
        void shouldThrowExceptionWhenErrorOccurs() {

            var userId = UUID.randomUUID();
            doThrow(new RuntimeException())
                    .when(repository)
                    .findById(any());

            assertThrows(
                    RuntimeException.class,
                    () ->  service.getUserById(userId.toString())
            );
        }
    }

    @Nested
    class listUsers {

        @Test
        @DisplayName("Should return all users")
        void shouldReturnAllUsers() {

            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "password",
                    Instant.now(),
                    null);
            var userList = List.of(user);
            doReturn(userList).when(repository).findAll();

            var output = service.listUsers();

            assertNotNull(output);
            assertEquals(userList.size(), output.size());
        }
    }

    @Nested
    class updateUser {

        @Test
        @DisplayName("Should update username and password with success when user exists")
        void shouldUpdateUsernameAndPassword() {

            // arrange
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "password",
                    Instant.now(),
                    null
            );
            var updateUser = new UpdateUserDto("user", "1234");
            var userId = user.getUserId().toString();

            doReturn(Optional.of(user))
                    .when(repository)
                    .findById(uuidArgumentCaptor.capture());
            doReturn(user).when(repository).save(userArgumentCaptor.capture());

            // act
            service.updateUser(userId, updateUser);

            // assert
            assertEquals(userId, uuidArgumentCaptor.getValue().toString());

            var userCaptured = userArgumentCaptor.getValue();
            assertEquals(updateUser.username(), userCaptured.getUsername());
            assertEquals(updateUser.password(), userCaptured.getPassword());

            verify(repository, times(1))
                    .findById(uuidArgumentCaptor.getValue());

            verify(repository, times(1)).save(user);
        }

        @Test
        @DisplayName("Should not update user when not exists")
        void shouldNotUpdateUserWhenNotExists() {

            // arrange
            var updateUser = new UpdateUserDto("user", "1234");
            var userId = UUID.randomUUID();

            doReturn(Optional.empty())
                    .when(repository)
                    .findById(uuidArgumentCaptor.capture());

            // act
            service.updateUser(userId.toString(), updateUser);

            // assert
            assertEquals(userId, uuidArgumentCaptor.getValue());

            verify(repository, times(1))
                    .findById(uuidArgumentCaptor.getValue());

            verify(repository, times(0)).save(any());
        }
    }

    @Nested
    class deleteUserById {

        @Test
        @DisplayName("Should delete user with success when user exists")
        void shouldDeleteUser() {

            doReturn(true)
                    .when(repository)
                    .existsById(uuidArgumentCaptor.capture());

            doNothing()
                    .when(repository)
                    .deleteById(uuidArgumentCaptor.capture());
            var userId = UUID.randomUUID();

            service.deleteUserById(userId.toString());

            var idList = uuidArgumentCaptor.getAllValues();
            assertEquals(userId, idList.get(0));
            assertEquals(userId, idList.get(1));

            verify(repository, times(1)).existsById(idList.get(0));
            verify(repository, times(1)).deleteById(idList.get(1));
        }

        @Test
        @DisplayName("Should not delete user when user not exists")
        void shouldNotDeleteUserWhenNotExists() {

            doReturn(false)
                    .when(repository)
                    .existsById(uuidArgumentCaptor.capture());
            var userId = UUID.randomUUID();

            service.deleteUserById(userId.toString());

            var capturedId = uuidArgumentCaptor.getValue();
            assertEquals(userId, capturedId);

            verify(repository, times(1)).existsById(capturedId);
            verify(repository, times(0)).deleteById(any());
        }
    }

}