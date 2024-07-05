package com.ufund.api.ufundapi.viewmodel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;


import com.ufund.api.ufundapi.model.User;
import com.ufund.api.ufundapi.persistence.UserFileDAO;

import java.io.IOException;
import java.util.List;

public class UserServiceTest {

    @Mock
    private UserFileDAO mockUserFileDAO;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(mockUserFileDAO);

    }

    @Test
    public void getUserByIdTest() {
        User expectedUser = new User("Alice", 1);
        when(mockUserFileDAO.getUser(1)).thenReturn(expectedUser);

        User result = userService.getUserByID(1);

        assertEquals(expectedUser, result);
    }

    @Test
    public void getUsersTest() throws IOException {
        User[] expectedUsers = {new User("Alice", 1), new User("Bob", 2)};
        when(mockUserFileDAO.getUsers()).thenReturn(expectedUsers);

        User[] result = userService.getUsers();

        assertArrayEquals(expectedUsers, result);
    }
    /*
    @Test
    public void addUserTest() throws IOException {
        User newUser = new User("Charlie", 3);
        when(mockUserFileDAO.addUser(newUser)).thenReturn(newUser);

        User result = userService.addUser(newUser);

        assertEquals(newUser, result);
    }
    */
    @Test
    public void updateUserTest() throws IOException {
        User updatedUser = new User("Dave", 4);
        when(mockUserFileDAO.updateUser(updatedUser)).thenReturn(updatedUser);

        User result = userService.updateUser(updatedUser);

        assertEquals(updatedUser, result);
    }

    @Test
    public void deleteUserTest() throws IOException {
        when(mockUserFileDAO.deleteUser(4)).thenReturn(true);

        boolean result = userService.deleteUser(4);

        assertTrue(result);
    }

    @Test
    public void findUsersTest() throws IOException {
        User[] expectedUsers = {new User("Eve", 5)};
        when(mockUserFileDAO.findUsers("Eve")).thenReturn(expectedUsers);

        User[] result = userService.findUsers("Eve");

        assertArrayEquals(expectedUsers, result);
    }
    // Testing IOException handling for updateUser method
    @Test
    public void updateUserIOExceptionTest() throws IOException {
        User updatedUser = new User("UpdatedUser", 7);
        when(mockUserFileDAO.updateUser(any(User.class))).thenThrow(new IOException());

        assertThrows(IOException.class, () -> userService.updateUser(updatedUser));
    }
    // Testing deleteUser method when UserFileDAO returns false (user not found)
    @Test
    public void deleteUserNotFoundTest() throws IOException {
        when(mockUserFileDAO.deleteUser(8)).thenReturn(false);

        boolean result = userService.deleteUser(8);

        assertFalse(result);
    }
    
    // Testing findUsers with a null search query
    @Test
    public void findUsersWithNullNameTest() throws IOException {
        User[] expectedUsers = new User[]{};
        when(mockUserFileDAO.findUsers(null)).thenReturn(expectedUsers);

        User[] result = userService.findUsers(null);

        assertArrayEquals(expectedUsers, result);
    }
    @Test
    public void addUserTest() throws IOException {
        User newUser = new User("Charlie", 3);
        when(mockUserFileDAO.addUser(any(User.class))).thenReturn(newUser);

        User result = userService.addUser(newUser);

        assertNotNull(result, "The result should not be null.");
        assertEquals(newUser.getName(), result.getName(), "The names should match.");
        assertEquals(newUser.getId(), result.getId(), "The IDs should match.");
        verify(mockUserFileDAO, times(1)).addUser(newUser);
    }
    @Test
    public void getUserIdByNameFoundTest() {
        String userName = "Eve";
        int expectedUserId = 5;
        when(mockUserFileDAO.getUserIdByName(userName)).thenReturn(expectedUserId);

        int result = userService.getUserIdByName(userName);

        assertEquals(expectedUserId, result, "The user ID should match the expected value.");
        verify(mockUserFileDAO, times(1)).getUserIdByName(userName);
    }
    @Test
    public void getUserIdByNameNotFoundTest() {
        String userName = "NonExistentUser";
        when(mockUserFileDAO.getUserIdByName(userName)).thenReturn(-1);

        int result = userService.getUserIdByName(userName);

        assertEquals(-1, result, "The method should return -1 when the user is not found.");
        verify(mockUserFileDAO, times(1)).getUserIdByName(userName);
    }
    @Test
    void makeUserPartner_Success() throws IOException {
        int userId = 1;
        when(mockUserFileDAO.addPartner(userId)).thenReturn(true);

        boolean result = userService.makeUserPartner(userId);

        assertTrue(result);
        verify(mockUserFileDAO).addPartner(userId);
    }

    @Test
    void makeUserPartner_Failure() throws IOException {
        int userId = 1;
        when(mockUserFileDAO.addPartner(userId)).thenReturn(false);

        boolean result = userService.makeUserPartner(userId);

        assertFalse(result);
        verify(mockUserFileDAO).addPartner(userId);
    }

    @Test
    void applyForPartnership_Success() throws IOException {
        int userId = 2;
        when(mockUserFileDAO.applyForPartnership(userId)).thenReturn(true);

        boolean result = userService.applyForPartnership(userId);

        assertTrue(result);
        verify(mockUserFileDAO).applyForPartnership(userId);
    }

    @Test
    void applyForPartnership_Failure() throws IOException {
        int userId = 2;
        when(mockUserFileDAO.applyForPartnership(userId)).thenReturn(false);

        boolean result = userService.applyForPartnership(userId);

        assertFalse(result);
        verify(mockUserFileDAO).applyForPartnership(userId);
    }

        @Test
    void getApplications_ReturnsList() throws IOException {
        List<Integer> expectedApplications = Arrays.asList(1, 2, 3);
        when(mockUserFileDAO.getApplications()).thenReturn(expectedApplications);

        List<Integer> result = userService.getApplications();

        assertEquals(expectedApplications, result);
        verify(mockUserFileDAO).getApplications();
    }

    @Test
    void getPartners_ReturnsList() throws IOException {
        List<Integer> expectedPartners = Arrays.asList(4, 5);
        when(mockUserFileDAO.getPartners()).thenReturn(expectedPartners);

        List<Integer> result = userService.getPartners();

        assertEquals(expectedPartners, result);
        verify(mockUserFileDAO).getPartners();
    }

    @Test
    void removeApplication_Success() throws IOException {
        int userId = 1;
        when(mockUserFileDAO.removeApplication(userId)).thenReturn(true);

        boolean result = userService.removeApplication(userId);

        assertTrue(result);
        verify(mockUserFileDAO).removeApplication(userId);
    }

    @Test
    void removeApplication_Failure() throws IOException {
        int userId = 1;
        when(mockUserFileDAO.removeApplication(userId)).thenReturn(false);

        boolean result = userService.removeApplication(userId);

        assertFalse(result);
        verify(mockUserFileDAO).removeApplication(userId);
    }



}
