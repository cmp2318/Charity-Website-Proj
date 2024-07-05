package com.ufund.api.ufundapi.viewmodel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.model.User;


import java.util.Arrays;


import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    private UserController userController;
    private UserService mockUserService;
    private BasketService mockBasketService;

    @BeforeEach
    public void setUp() {
        mockUserService = mock(UserService.class);
        mockBasketService = mock(BasketService.class);
        userController = new UserController(mockUserService);
    }

    // Adjusted to create User objects according to your User class definition
    @Test
    public void testGetAllUsersSuccess() throws IOException {
        User[] users = {new User("Alice", 1), new User("Bob", 2)};
        when(mockUserService.getUsers()).thenReturn(users);

        ResponseEntity<User[]> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(users, response.getBody());
    }

    @Test
    public void testGetUserFound() throws IOException {
        User user = new User("Charlie", 3);
        when(mockUserService.getUserByID(3)).thenReturn(user);

        ResponseEntity<User> response = userController.getUser(3);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testCreateUserSuccess() throws IOException {
        User newUser = new User("Dave", 4);
        when(mockUserService.findUsers("Dave")).thenReturn(new User[]{});
        when(mockUserService.addUser(any(User.class))).thenReturn(newUser);

        ResponseEntity<User> response = userController.addUser(newUser);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testSearchUsersSuccess() throws IOException {
        User[] users = {new User("Eve", 5), new User("Frank", 6)};
        when(mockUserService.findUsers("e")).thenReturn(users);

        ResponseEntity<User[]> response = userController.searchUsers("e");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(users, response.getBody());
    }

        // Error handling for getAllUsers when an IOException occurs
    @Test
    public void testGetAllUsersFailure() throws IOException {
        when(mockUserService.getUsers()).thenThrow(new IOException());

        ResponseEntity<User[]> response = userController.getAllUsers();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // User not found scenario for getUser
    @Test
    public void testGetUserNotFound() throws IOException {
        when(mockUserService.getUserByID(999)).thenReturn(null);

        ResponseEntity<User> response = userController.getUser(999);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    // Conflict scenario for createUser when a user with the same name already exists
    @Test
    public void testCreateUserConflict() throws IOException {
        User existingUser = new User("ExistingUser", 10);
        when(mockUserService.findUsers(existingUser.getName())).thenReturn(new User[]{existingUser});

        ResponseEntity<User> response = userController.addUser(new User("ExistingUser", 11));

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    // Error handling for createUser when an IOException occurs
    @Test
    public void testCreateUserFailure() throws IOException {
        User newUser = new User("NewUser", 12);
        when(mockUserService.findUsers(anyString())).thenThrow(new IOException());

        ResponseEntity<User> response = userController.addUser(newUser);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    // Error handling for deleteUser when an IOException occurs
    @Test
    public void testDeleteUserFailure() throws IOException {
        doThrow(new IOException()).when(mockUserService).deleteUser(anyInt());

        ResponseEntity<User> response = userController.deleteUser(1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // Error handling for searchUsers when an IOException occurs
    @Test
    public void testSearchUsersFailure() throws IOException {
        when(mockUserService.findUsers(anyString())).thenThrow(new IOException());

        ResponseEntity<User[]> response = userController.searchUsers("test");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // User not found scenario for updateUser
   

    // Error handling for updateUser when an IOException occurs
    @Test
    public void testUpdateUserFailure() throws IOException {
        User userToUpdate = new User("UserToUpdate", 14);
        when(mockUserService.updateUser(any(User.class))).thenThrow(new IOException());

        ResponseEntity<User> response = userController.updateUser(userToUpdate);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUpdateUserSuccess() throws IOException {
        User existingUser = new User("Existing User",1);
        when(mockUserService.updateUser(any(User.class))).thenReturn(existingUser);

        ResponseEntity<User> response = userController.updateUser(existingUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(existingUser, response.getBody());
    }

    @Test
    public void testUpdateUserNotFound() throws IOException {
        when(mockUserService.updateUser(any(User.class))).thenReturn(null);

        ResponseEntity<User> response = userController.updateUser(new User( "Non-existing User",2));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteUserSuccess() throws IOException {
        when(mockUserService.deleteUser(1)).thenReturn(true);

        ResponseEntity<User> response = userController.deleteUser(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteUserNotFound() throws IOException {
        when(mockUserService.deleteUser(anyInt())).thenReturn(false);

        ResponseEntity<User> response = userController.deleteUser(999);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetUserIdByNameFound() {
        when(mockUserService.getUserIdByName("Existing User")).thenReturn(1);

        ResponseEntity<Integer> response = userController.getUserIdByName("Existing User");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "The response body should not be null.");
        assertEquals(Integer.valueOf(1), response.getBody(), "The ID should match the expected user ID.");
    }

    @Test
    public void testGetUserIdByNameNotFound() {
        when(mockUserService.getUserIdByName("Non-existing User")).thenReturn(-1);

        ResponseEntity<Integer> response = userController.getUserIdByName("Non-existing User");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Integer.valueOf(-1),response.getBody(), "The response body should be null for a non-existing user.");
    }
    @Test
    void makeUserPartner_Success() throws IOException {
        int userId = 1;
        when(mockUserService.makeUserPartner(userId)).thenReturn(true);
        when(mockUserService.removeApplication(userId)).thenReturn(true);

        ResponseEntity<?> response = userController.makeUserPartner(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(mockUserService).makeUserPartner(userId);
        verify(mockUserService).removeApplication(userId);
    }

    @Test
    void makeUserPartner_Failure() throws IOException {
        int userId = 1;
        when(mockUserService.makeUserPartner(userId)).thenReturn(false);

        ResponseEntity<?> response = userController.makeUserPartner(userId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void applyForPartnership_AlreadyApplied() throws IOException {
        int userId = 1;
        List<Integer> currentApplications = Collections.singletonList(userId);
        when(mockUserService.getApplications()).thenReturn(currentApplications);

        ResponseEntity<?> response = userController.applyForPartnership(userId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void applyForPartnership_Success() throws IOException {
        int userId = 2;
        when(mockUserService.getApplications()).thenReturn(Collections.singletonList(1));
        when(mockUserService.applyForPartnership(userId)).thenReturn(true);

        ResponseEntity<?> response = userController.applyForPartnership(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getApplications_Success() throws IOException {
        List<Integer> applications = Arrays.asList(1, 2, 3);
        when(mockUserService.getApplications()).thenReturn(applications);

        ResponseEntity<List<Integer>> response = userController.getApplications();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(applications, response.getBody());
    }

    @Test
    void getApplications_Error() throws IOException {
        when(mockUserService.getApplications()).thenThrow(new IOException());

        ResponseEntity<List<Integer>> response = userController.getApplications();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void getPartners_Success() throws IOException {
        List<Integer> partners = Arrays.asList(1, 4);
        when(mockUserService.getPartners()).thenReturn(partners);

        ResponseEntity<List<Integer>> response = userController.getPartners();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(partners, response.getBody());
    }

    @Test
    void getPartners_Error() throws IOException {
        when(mockUserService.getPartners()).thenThrow(new IOException());

        ResponseEntity<List<Integer>> response = userController.getPartners();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }




}
