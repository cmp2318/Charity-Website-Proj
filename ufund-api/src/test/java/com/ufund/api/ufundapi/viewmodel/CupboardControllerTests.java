package com.ufund.api.ufundapi.viewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.model.Toy;


@Tag("Controller-tier")
public class CupboardControllerTests {
    private CupboardController CupboardController;
    private CupboardService mockCupboardService;

    @BeforeEach
    public void setupCupboardController() {
        mockCupboardService = mock(CupboardService.class);
        CupboardController = new CupboardController(mockCupboardService);
    }

    @Test
    public void testGetCupboard() throws IOException {
        // Setup
        int ToyId = 1;
        Toy Toy = new Toy(ToyId,"name",0,0,"type");
        when(mockCupboardService.getToyByID(ToyId)).thenReturn(Toy);

        // Invoke
        ResponseEntity<Toy> response = CupboardController.getToy(ToyId);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Toy, response.getBody());
    }

    @Test
    public void testGetToyNotFound() throws IOException {
        // Setup
        int ToyId = 1;
        when(mockCupboardService.getToyByID(ToyId)).thenReturn(null);

        // Invoke
        ResponseEntity<Toy> response = CupboardController.getToy(ToyId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAllCupboard() throws IOException {
        // Setup
        int ToyId = 1;
        Toy Toy = new Toy(ToyId,"name",0,0,"type");
        Toy[] toy = new Toy[1];
        toy[0] = Toy;
        when(mockCupboardService.getAllToys()).thenReturn(toy);

        // Invoke
        ResponseEntity<Toy[]> response = CupboardController.getAllToys();

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(toy, response.getBody());
    }
    @Test
    public void testGetAllToysThrowsIOException() throws IOException {
        when(mockCupboardService.getAllToys()).thenThrow(new IOException());

        ResponseEntity<Toy[]> response = CupboardController.getAllToys();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetToyHandleException() throws RuntimeException {
        int toyId = 1;

        doThrow(new RuntimeException()).when(mockCupboardService).getToyByID(toyId);

        ResponseEntity<Toy> response = CupboardController.getToy(toyId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    @Test
    public void testCreateToySuccess() throws IOException {
        Toy toy = new Toy(1, "NewToy", 10, 5, "Type");
        Toy[] noToys = new Toy[]{};

        when(mockCupboardService.findToys("NewToy")).thenReturn(noToys);
        when(mockCupboardService.createToy(toy)).thenReturn(toy);

        ResponseEntity<Toy> response = CupboardController.createToy(toy);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(toy, response.getBody());
    }

    @Test
    public void testCreateToyConflict() throws IOException {
        Toy toy = new Toy(1, "NewToy",0,0,"Type");
        Toy[] toys = {toy};
        when(mockCupboardService.findToys("NewToy")).thenReturn(toys);

        ResponseEntity<Toy> response = CupboardController.createToy(toy);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testCreateToyThrowsIOException() throws IOException {
        Toy toy = new Toy(1, "NewToy",0,0,"Type");
        
        when(mockCupboardService.findToys("NewToy")).thenThrow(new IOException());

        ResponseEntity<Toy> response = CupboardController.createToy(toy);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    @Test
    public void testUpdateToy() throws IOException {
        // Setup
        Toy updatedToy = new Toy(6,"SuperMan",2,10,"Action Figure");
        
        when(mockCupboardService.updateToy(updatedToy)).thenReturn(updatedToy);

        // Invoke
        ResponseEntity<Toy> response = CupboardController.updateToy(updatedToy);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedToy, response.getBody());
    }

    @Test
    public void testUpdateToyNotFound() throws IOException {
        // Setup
        Toy toy = new Toy(1, "Teddy Bear",1,15,"Plush");
        when(mockCupboardService.updateToy(toy)).thenReturn(null);

        // Invoke
        ResponseEntity<Toy> response = CupboardController.updateToy(toy);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateToyHandleException() throws IOException {
        // Setup
        Toy toy = new Toy(1, "Teddy Bear",10,10,"Plush");
        doThrow(new IOException()).when(mockCupboardService).updateToy(toy);

        // Invoke
        ResponseEntity<Toy> response = CupboardController.updateToy(toy);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testdeleteToy() throws IOException {
        // Setup
        int ToyId = 1;
        when(mockCupboardService.deleteToy(ToyId)).thenReturn(true);

        // Invoke
        ResponseEntity<Toy> response = CupboardController.deleteToy(ToyId);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testdeleteToyNotFound() throws IOException {
        // Setup
        int ToyId = 1;
        when(mockCupboardService.deleteToy(ToyId)).thenReturn(false);

        // Invoke
        ResponseEntity<Toy> response = CupboardController.deleteToy(ToyId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testdeleteToyHandleException() throws IOException {
        // Setup
        int ToyId = 1;
        doThrow(new IOException()).when(mockCupboardService).deleteToy(ToyId);

        // Invoke
        ResponseEntity<Toy> response = CupboardController.deleteToy(ToyId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
    @Test
    public void testSearchToys() throws IOException {
        Toy[] toys = {new Toy(1, "Match",10,10,"Plush")};
        when(mockCupboardService.findToys("Match")).thenReturn(toys);

        ResponseEntity<Toy[]> response = CupboardController.searchToys("Match");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(toys, response.getBody());
    }

    @Test
    public void testSearchToysThrowsIOException() throws IOException {
        when(mockCupboardService.findToys(null)).thenThrow(new IOException());

        ResponseEntity<Toy[]> response = CupboardController.searchToys("Match");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


}
