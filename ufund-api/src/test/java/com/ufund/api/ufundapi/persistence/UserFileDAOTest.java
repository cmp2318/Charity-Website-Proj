package com.ufund.api.ufundapi.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class UserFileDAOTest {

    @Mock
    private ObjectMapper mockObjectMapper;

    private UserFileDAO userFileDAO;

    private Path tempDir;

    private Path tempPartnerDir;

    private Path tempAppDir;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        tempDir = Files.createTempDirectory("testUsersDir");
        File tempFile = tempDir.resolve("testUsers.json").toFile();

        tempPartnerDir = Files.createTempDirectory("testPartnersDir"); 
        File tempPartnerFile = tempPartnerDir.resolve("testPartners.json").toFile();

        tempAppDir = Files.createTempDirectory("testAppDir");
        File tempAppFile = tempAppDir.resolve("testApps.json").toFile();

        // Ensure this mock returns an empty array to avoid NullPointerException
        when(mockObjectMapper.readValue(any(File.class), eq(User[].class))).thenReturn(new User[0]);

        when(mockObjectMapper.readValue(any(File.class), eq(new TypeReference<List<Integer>>(){})))
        .thenReturn(new ArrayList<>());

        userFileDAO = new UserFileDAO(tempFile.getAbsolutePath(), 
            tempAppFile.getAbsolutePath(), tempPartnerFile.getAbsolutePath(), mockObjectMapper);
    }



    @AfterEach
    public void tearDown() throws IOException {
        Files.walk(tempDir)
             .sorted(Comparator.reverseOrder())
             .map(Path::toFile)
             .forEach(File::delete);
    }

    @Test
    public void getUserByIdTest() throws IOException {
        User mockUser = new User("Test User", 1);
        userFileDAO.users.put(1, mockUser);

        User result = userFileDAO.getUser(1);

        assertNotNull(result);
        assertEquals(mockUser.getName(), result.getName());
        assertEquals(mockUser.getId(), result.getId());
    }

    @Test
    public void getUsersTest() throws IOException {
        User[] expectedUsers = {
            new User("Alice", 1),
            new User("Bob", 2)
        };
        when(mockObjectMapper.readValue(any(File.class), eq(User[].class))).thenReturn(expectedUsers);

        User[] users = userFileDAO.getUsers();

        verify(mockObjectMapper).readValue(any(File.class), eq(User[].class));
        assertNotNull(users);
        assertEquals(0, users.length); 
    }

    @Test
    public void addUserTest() throws IOException {
        User newUser = new User("New User", 3);
        doNothing().when(mockObjectMapper).writeValue(any(File.class), any());
        User result = userFileDAO.addUser(newUser);

        assertNotNull(result);
        assertEquals("New User", result.getName());
        // Verify that the user has been assigned an ID, assuming IDs are auto-generated
        assertTrue(result.getId() > 0);
    }

    @Test
    public void updateUserTest() throws IOException {
        User originalUser = new User("Original Name", 4);
        userFileDAO.users.put(4, originalUser);

        User updatedUser = new User("Updated Name", 4);
        User result = userFileDAO.updateUser(updatedUser);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals(4, result.getId());
    }

    @Test
    public void updateUserTestNull() throws IOException{
        User nonExistentUser = new User("Name", -1); 
        
        User result = userFileDAO.updateUser(nonExistentUser);
        
        assertNull(result, "Updating a non-existent user should return null.");
    }
    

    @Test
    public void deleteUserTest() throws IOException {
        User userToDelete = new User("Delete Me", 5);
        userFileDAO.users.put(5, userToDelete);

        boolean result = userFileDAO.deleteUser(5);

        assertTrue(result);
        assertFalse(userFileDAO.users.containsKey(5));
    }

    @Test
    public void findUsersTest() throws IOException {
        User[] mockUsers = {new User("Alice", 1), new User("Alex", 2), new User("Bob", 3)};
        when(mockObjectMapper.readValue(any(File.class), eq(User[].class))).thenReturn(mockUsers);

        // Reload users to apply mocked users array
        userFileDAO = new UserFileDAO(tempDir.resolve("testUsers.json").toString(), 
        "ApplicationsFilename", "partnersFilename", mockObjectMapper);

        User[] foundUsers = userFileDAO.findUsers("Al");
        assertEquals(2, foundUsers.length, "Expected to find 2 users containing 'Al' in their name.");
        assertTrue(foundUsers[0].getName().contains("Al") && foundUsers[1].getName().contains("Al"), "Found users should have 'Al' in their names.");
    }




    @Test
    public void addUserIOExceptionTest() throws IOException {
        doThrow(IOException.class).when(mockObjectMapper).writeValue(any(File.class), any());

        User newUser = new User("New User", 3);

        Exception exception = assertThrows(IOException.class, () -> userFileDAO.addUser(newUser),
            "Expected addUser to throw IOException");
        assertNotNull(exception);
    }

    @Test
    public void updateUserIOExceptionTest() throws IOException {
        doThrow(IOException.class).when(mockObjectMapper).writeValue(any(File.class), any());

        User userToUpdate = new User("Existing User", 4);
        // Assuming the user exists for the update to proceed
        userFileDAO.users.put(4, userToUpdate);

        Exception exception = assertThrows(IOException.class, () -> userFileDAO.updateUser(userToUpdate),
            "Expected updateUser to throw IOException");
        assertNotNull(exception);
    }

    @Test
    public void deleteUserIOExceptionTest() throws IOException {
        User userToDelete = new User("Delete Me", 5);
        userFileDAO.users.put(5, userToDelete);
        doThrow(IOException.class).when(mockObjectMapper).writeValue(any(File.class), any());

        Exception exception = assertThrows(IOException.class, () -> userFileDAO.deleteUser(5),
            "Expected deleteUser to throw IOException");
        assertNotNull(exception);
    }

    @Test
    public void getUserNonExistentTest() {
        assertNull(userFileDAO.getUser(999), "Expected null for non-existent user ID.");
    }

    @Test
    public void getUserIdByName_userFound() {
        
        User existingUser = new User("Alice", 1);
        userFileDAO.users.put(1, existingUser);

        int userId = userFileDAO.getUserIdByName("Alice");

        assertEquals(1, userId, "The ID should match the expected value for the user 'Alice'.");
    }

    @Test
    public void getUserIdByName_userNotFound() {
        int userId = userFileDAO.getUserIdByName("NonExistingUser");
        assertEquals(-1, userId, "The method should return -1 when the user is not found.");
    }
    @Test
    public void getUserIdByName_caseInsensitive() {
        User existingUser = new User("Alice", 1);
        userFileDAO.users.put(1, existingUser);

        assertEquals(1, userFileDAO.getUserIdByName("alice"), "Search should be case-insensitive.");
        assertEquals(1, userFileDAO.getUserIdByName("ALICE"), "Search should be case-insensitive.");
        assertEquals(1, userFileDAO.getUserIdByName("AlIcE"), "Search should be case-insensitive.");
    }

    @Test
    public void testAddPartner() throws IOException {
        int testUserId = 10; // Example user ID
        List<Integer> initialApplications = new ArrayList<>(); // Assume starting with an empty list

        when(mockObjectMapper.readValue(any(File.class), eq(new TypeReference<List<Integer>>(){})))
            .thenReturn(initialApplications);

        boolean result = userFileDAO.applyForPartnership(testUserId);

        assertTrue(result, "Application should be successful");

        ArgumentCaptor<List<Integer>> argument = ArgumentCaptor.forClass(List.class);
        verify(mockObjectMapper).writeValue(any(File.class), argument.capture());

        List<Integer> updatedApplications = argument.getValue();
        assertTrue(updatedApplications.contains(testUserId), "The application list should contain the test user's ID");
    }   

    @Test
    public void addPartnerTest() throws IOException {
        int testUserId = 42; // Example user ID
        List<Integer> initialPartnerIds = new ArrayList<>();
        initialPartnerIds.add(43);

        when(mockObjectMapper.readValue(any(File.class), eq(new TypeReference<List<Integer>>(){})))
            .thenReturn(initialPartnerIds);

        // Call the method under test
        boolean result = userFileDAO.addPartner(testUserId);

        // Assert the result is true
        assertTrue(result, "Expected addPartner to return true indicating the user was added successfully");

        // Capture the list of partner IDs written to the file
        ArgumentCaptor<List<Integer>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mockObjectMapper).writeValue(any(File.class), argumentCaptor.capture());
        List<Integer> capturedPartnerIds = argumentCaptor.getValue();

        // Assert the test user ID was added to the list
        assertTrue(capturedPartnerIds.contains(testUserId), "The updated partner IDs should include the test user ID");
    }

    @Test
    public void applyForPartnershipTest() throws IOException {
        int testUserId = 99; // Example user ID for testing
        List<Integer> initialApplications = new ArrayList<>(); // Starting with an empty list of applications

        // Mock the objectMapper to simulate reading an empty list of applications from the file
        when(mockObjectMapper.readValue(any(File.class), eq(new TypeReference<List<Integer>>(){})))
            .thenReturn(initialApplications);

        // Call the applyForPartnership method and assert it returns true
        boolean result = userFileDAO.applyForPartnership(testUserId);
        assertTrue(result, "Expected applyForPartnership to return true indicating the application was added successfully");

        // Capture and assert that the updated list of applications was written to the file with the test user ID included
        ArgumentCaptor<List<Integer>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mockObjectMapper).writeValue(any(File.class), argumentCaptor.capture());
        List<Integer> capturedApplications = argumentCaptor.getValue();

        // Assert the test user ID was added to the list of applications
        assertTrue(capturedApplications.contains(testUserId), "The updated list of applications should include the test user ID");
    }

    @Test
    public void testGetApplicationsFileDNE() throws IOException {

        List<Integer> resultApplications = userFileDAO.getApplications();

        assertNotNull(resultApplications, "Expected non-null list of applications");
        assertTrue(resultApplications.isEmpty(), "Expected an empty list when the file does not exist");
    }

    @Test
    public void testDeleteUser() throws IOException {
        int userIdToDelete = 1;
        User userToDelete = new User("Test User", userIdToDelete);
        userFileDAO.users.put(userIdToDelete, userToDelete);

        boolean result = userFileDAO.deleteUser(userIdToDelete);

        assertTrue(result, "User deletion should return true");
        assertFalse(userFileDAO.users.containsKey(userIdToDelete), "User should be removed from the map");
        verify(mockObjectMapper).writeValue(any(File.class), any(User[].class));
    }

    @Test
    public void testDeleteNonExistentUser() throws IOException {
        int nonExistentUserId = 999;

        boolean result = userFileDAO.deleteUser(nonExistentUserId);

        assertFalse(result, "Deleting a non-existent user should return false");
        assertFalse(userFileDAO.users.containsKey(nonExistentUserId), "User map should remain unchanged");
        verify(mockObjectMapper, never()).writeValue(any(File.class), any(User[].class));
    }

    @Test
    public void testGetPartnersFileNotFound() throws IOException {
        when(mockObjectMapper.readValue(any(File.class), eq(new TypeReference<List<Integer>>() {})))
                .thenThrow(new IOException()); // Simulate file not found

        List<Integer> actualPartners = userFileDAO.getPartners();

        assertNotNull(actualPartners, "Retrieved partners should not be null when file not found");
        assertTrue(actualPartners.isEmpty(), "Retrieved partners list should be empty when file not found");
    }

    @Test
    public void testRemoveApplicationIDNotFound() throws IOException {
        
        List<Integer> existingApplications = new ArrayList<>();
        existingApplications.add(42); // Add a sample application ID

        when(mockObjectMapper.readValue(any(File.class), eq(new TypeReference<List<Integer>>() {})))
                .thenReturn(existingApplications);

        // Call the method under test
        boolean result = userFileDAO.removeApplication(500); // Passing different id

        // Verify that the application was removed
        assertFalse(result);
    }


}
