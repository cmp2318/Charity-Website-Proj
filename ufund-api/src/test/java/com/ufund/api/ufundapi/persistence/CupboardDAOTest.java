package com.ufund.api.ufundapi.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ufund.api.ufundapi.model.Toy;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
public class CupboardDAOTest {
    
    @Mock
    private CupboardDAO cupboardDAO;

    // Example toy for use in tests
    private Toy exampleToy;

    @BeforeEach
    public void setUp() {
        // Initialize a Toy object with known values
        exampleToy = new Toy(1, "Action Figure", 5, 200, "Figure");
    }

    @Test
    public void testGetToys() throws IOException {
        // Arrange
        when(cupboardDAO.getToys()).thenReturn(new Toy[]{exampleToy});

        // Act
        Toy[] toys = cupboardDAO.getToys();

        // Assert
        assertArrayEquals(new Toy[]{exampleToy}, toys, "getToys should return the correct array of Toys.");
    }

    @Test
    public void testFindToys() throws IOException {
        // Arrange
        when(cupboardDAO.findToys("Action")).thenReturn(new Toy[]{exampleToy});

        // Act
        Toy[] foundToys = cupboardDAO.findToys("Action");

        // Assert
        assertArrayEquals(new Toy[]{exampleToy}, foundToys, "findToys should return Toys containing the text.");
    }

    @Test
    public void testGetToy() throws IOException {
        // Arrange
        when(cupboardDAO.getToy(1)).thenReturn(exampleToy);

        // Act
        Toy foundToy = cupboardDAO.getToy(1);

        // Assert
        assertEquals(exampleToy, foundToy, "getToy should return the correct Toy by ID.");
    }

    @Test
    public void testCreateToy() throws IOException {
        // Arrange
        when(cupboardDAO.createToy(any(Toy.class))).thenReturn(exampleToy);

        // Act
        Toy createdToy = cupboardDAO.createToy(new Toy(0, "Teddy Bear", 3, 100, "Plush"));

        // Assert
        assertNotNull(createdToy, "createToy should return a Toy object on success.");
        assertEquals(exampleToy, createdToy, "The created Toy should match the expected Toy.");
    }

    @Test
    public void testUpdateToy() throws IOException {
        // Arrange
        when(cupboardDAO.updateToy(any(Toy.class))).thenReturn(exampleToy);

        // Act
        Toy updatedToy = cupboardDAO.updateToy(exampleToy);

        // Assert
        assertEquals(exampleToy, updatedToy, "updateToy should return the updated Toy object.");
    }

    @Test
    public void testDeleteToy() throws IOException {
        // Arrange
        when(cupboardDAO.deleteToy(1)).thenReturn(true);

        // Act
        boolean result = cupboardDAO.deleteToy(1);

        // Assert
        assertTrue(result, "deleteToy should return true if the Toy was successfully deleted.");
    }

    @Test
    public void testIOException() throws IOException {
        // Arrange
        when(cupboardDAO.getToys()).thenThrow(IOException.class);

        // Act & Assert
        assertThrows(IOException.class, () -> cupboardDAO.getToys(), "getToys should throw IOException on underlying storage issues.");
    }
    
    @Test
    public void testGetToysWhenEmpty() throws IOException {
        // Arrange
        when(cupboardDAO.getToys()).thenReturn(new Toy[]{});

        // Act
        Toy[] toys = cupboardDAO.getToys();

        // Assert
        assertEquals(0, toys.length, "getToys should return an empty array when there are no toys.");
    }

    @Test
    public void testFindToysWithNoMatches() throws IOException {
        // Arrange
        when(cupboardDAO.findToys("NonExistent")).thenReturn(new Toy[]{});

        // Act
        Toy[] foundToys = cupboardDAO.findToys("NonExistent");

        // Assert
        assertEquals(0, foundToys.length, "findToys should return an empty array when no toys match the criteria.");
    }

    @Test
    public void testGetToyNotFound() throws IOException {
        // Arrange
        when(cupboardDAO.getToy(999)).thenReturn(null);

        // Act
        Toy foundToy = cupboardDAO.getToy(999);

        // Assert
        assertNull(foundToy, "getToy should return null when the toy ID does not exist.");
    }

    @Test
    public void testUpdateToyNotFound() throws IOException {
        // Arrange
        when(cupboardDAO.updateToy(any(Toy.class))).thenReturn(null);

        // Act
        Toy result = cupboardDAO.updateToy(new Toy(999, "NonExistent", 0, 0, "NonExistentType"));

        // Assert
        assertNull(result, "updateToy should return null when the toy to update does not exist.");
    }

    @Test
    public void testDeleteToyNotFound() throws IOException {
        // Arrange
        when(cupboardDAO.deleteToy(999)).thenReturn(false);

        // Act
        boolean result = cupboardDAO.deleteToy(999);

        // Assert
        assertFalse(result, "deleteToy should return false if the toy does not exist.");
    }

    // Exception scenarios for each method...
    @Test
    public void testFindToysIOException() throws IOException {
        // Arrange
        when(cupboardDAO.findToys("Action")).thenThrow(IOException.class);

        // Act & Assert
        assertThrows(IOException.class, () -> cupboardDAO.findToys("Action"), "findToys should throw IOException on underlying storage issues.");
    }

    @Test
    public void testGetToyIOException() throws IOException {
        // Arrange
        when(cupboardDAO.getToy(1)).thenThrow(IOException.class);

        // Act & Assert
        assertThrows(IOException.class, () -> cupboardDAO.getToy(1), "getToy should throw IOException on underlying storage issues.");
    }

    @Test
    public void testCreateToyIOException() throws IOException {
        // Arrange
        when(cupboardDAO.createToy(any(Toy.class))).thenThrow(IOException.class);

        // Act & Assert
        assertThrows(IOException.class, () -> cupboardDAO.createToy(exampleToy), "createToy should throw IOException on underlying storage issues.");
    }

    @Test
    public void testUpdateToyIOException() throws IOException {
        // Arrange
        when(cupboardDAO.updateToy(any(Toy.class))).thenThrow(IOException.class);

        // Act & Assert
        assertThrows(IOException.class, () -> cupboardDAO.updateToy(exampleToy), "updateToy should throw IOException on underlying storage issues.");
    }

    @Test
    public void testDeleteToyIOException() throws IOException {
   
            when(cupboardDAO.deleteToy(1)).thenThrow(IOException.class);
    

        // Act & Assert
        assertThrows(IOException.class, () -> cupboardDAO.deleteToy(1), "deleteToy should throw IOException on underlying storage issues.");
    }





}
