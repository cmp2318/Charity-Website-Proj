package com.ufund.api.ufundapi.viewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.model.EmailRequest;
import com.ufund.api.ufundapi.model.FundBasket;
import com.ufund.api.ufundapi.model.Toy;


@Tag("Controller-tier")
public class BasketControllerTest {
    private BasketController basketController;
    private BasketService mockBasketService;
    private EmailService mockEmailService;

    @BeforeEach
    public void setupbasketController() {
        mockBasketService = mock(BasketService.class);
        mockEmailService = mock(EmailService.class);
        basketController = new BasketController(mockBasketService, mockEmailService);
    }

    @Test
    public void testgetBasket() throws IOException {
        // Setup
        int basketId = 1;
        FundBasket basket = new FundBasket(basketId);
        when(mockBasketService.getBasket(basketId)).thenReturn(basket);

        // Invoke
        ResponseEntity<FundBasket> response = basketController.getBasket(basketId);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(basket, response.getBody());
    }

    @Test
    public void testGetBasketNotFound() throws IOException {
        // Setup
        int basketId = 1;
        when(mockBasketService.getBasket(basketId)).thenReturn(null);

        // Invoke
        ResponseEntity<FundBasket> response = basketController.getBasket(basketId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetBasketHandleException() throws IOException {
        // Setup
        int basketId = 1;
        doThrow(new IOException()).when(mockBasketService).getBasket(basketId);

        // Invoke
        ResponseEntity<FundBasket> response = basketController.getBasket(basketId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testCreateBasket() throws IOException {
        // Setup
        FundBasket newBasket = new FundBasket(1);
        when(mockBasketService.createBasket()).thenReturn(newBasket);

        // Invoke
        ResponseEntity<FundBasket> response = basketController.createBasket();

        // Analyze
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newBasket, response.getBody());
    }

    @Test
    public void testcreateBasketHandleException() throws IOException {
        // Setup
        doThrow(new IOException()).when(mockBasketService).createBasket();

        // Invoke
        ResponseEntity<FundBasket> response = basketController.createBasket();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testAddToy() throws IOException {
        // Setup
        int basketId = 1;
        Toy toy = new Toy(6,"SuperMan",1,10,"Action Figure");
        FundBasket updatedBasket = new FundBasket(basketId);
        updatedBasket.getBasket().add(toy);
        when(mockBasketService.addToyBasket(basketId, toy)).thenReturn(updatedBasket);

        // Invoke
        ResponseEntity<FundBasket> response = basketController.addToy(basketId, toy);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedBasket, response.getBody());
    }

    @Test
    public void testAddToyNotFound() throws IOException {
        // Setup
        int basketId = 1;
        Toy toy = new Toy(1, "Teddy Bear",1,15,"Plush");
        when(mockBasketService.addToyBasket(basketId, toy)).thenReturn(null);

        // Invoke
        ResponseEntity<FundBasket> response = basketController.addToy(basketId, toy);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testToyDup() throws IOException {

        int basketId = 1;
        Toy toy = new Toy(1, "Teddy Bear", 1,10,"Plush"); // Initial quantity is 1
        FundBasket initialBasket = new FundBasket(basketId);
        initialBasket.getBasket().add(toy);

        when(mockBasketService.getBasket(basketId)).thenReturn(initialBasket);
        when(mockBasketService.addToyBasket(eq(basketId), eq(toy))).thenAnswer(invocation -> {
        // Retrieve the arguments passed to the method call

        Toy toyToAdd = invocation.getArgument(1);

        // Simulate adding the toy to the basket again with increased quantity
        Toy existingToy = initialBasket.getBasket().get(0);
        if (existingToy != null) {
            // If the toy already exists in the basket, increase its quantity
            existingToy.increaseQuantity(1);
        } else {
            // Otherwise, add the toy to the basket
            initialBasket.getBasket().add(toyToAdd);
        }
        return initialBasket;
    });
        // Invoke
        ResponseEntity<FundBasket> response = basketController.addToy(basketId, toy); // Add the toy again

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getBasket().get(0).getQuantity()); // Check if quantity has been updated
    }

    @Test
    public void testAddToyHandleException() throws IOException {
        // Setup
        int basketId = 1;
        Toy toy = new Toy(1, "Teddy Bear",10,10,"Plush");
        doThrow(new IOException()).when(mockBasketService).addToyBasket(basketId, toy);

        // Invoke
        ResponseEntity<FundBasket> response = basketController.addToy(basketId, toy);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testRemoveToy() throws IOException {
        // Setup
        int basketId = 1;
        int toyId = 1;
        when(mockBasketService.removeToy(basketId, toyId)).thenReturn(true);

        // Invoke
        ResponseEntity<FundBasket> response = basketController.removeToy(basketId, toyId);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testRemoveToyNotFound() throws IOException {
        // Setup
        int basketId = 1;
        int toyId = 1;
        when(mockBasketService.removeToy(basketId, toyId)).thenReturn(false);

        // Invoke
        ResponseEntity<FundBasket> response = basketController.removeToy(basketId, toyId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testRemoveToyHandleException() throws IOException {
        // Setup
        int basketId = 1;
        int toyId = 1;
        doThrow(new IOException()).when(mockBasketService).removeToy(basketId, toyId);

        // Invoke
        ResponseEntity<FundBasket> response = basketController.removeToy(basketId, toyId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDeleteBasket() throws IOException {
        // Setup
        int basketId = 1;
        when(mockBasketService.deleteBasket(basketId)).thenReturn(true);

        // Invoke
        ResponseEntity<FundBasket> response = basketController.deleteBasket(basketId);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteBasketNotFound() throws IOException {
        // Setup
        int basketId = 1;
        when(mockBasketService.deleteBasket(basketId)).thenReturn(false);

        // Invoke
        ResponseEntity<FundBasket> response = basketController.deleteBasket(basketId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteBasketHandleException() throws IOException {
        // Setup
        int basketId = 1;
        doThrow(new IOException()).when(mockBasketService).deleteBasket(basketId);

        // Invoke
        ResponseEntity<FundBasket> response = basketController.deleteBasket(basketId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetAllBaskets() throws IOException {
        // Setup
        FundBasket[] baskets = { new FundBasket(1), new FundBasket(2) };
        when(mockBasketService.getAllBaskets()).thenReturn(baskets);

        // Invoke
        ResponseEntity<FundBasket[]> response = basketController.getAllBaskets();

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(baskets, response.getBody());
    }

    @Test
    public void testSendReceipt_Success() {
        // Arrange
        EmailRequest emailRequest = new EmailRequest("test@example.com", "Receipt body");
        when(mockEmailService.sendEmail(anyString(), anyString())).thenReturn(true);

        // Act
        ResponseEntity<Void> response = basketController.sendReceipt(emailRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(mockEmailService, times(1)).sendEmail("test@example.com", "Receipt body");
    }

    @Test
    public void testSendReceipt_Failure() {
        // Arrange
        EmailRequest emailRequest = new EmailRequest("test@example.com", "Receipt body");
        when(mockEmailService.sendEmail(anyString(), anyString())).thenReturn(false);

        // Act
        ResponseEntity<Void> response = basketController.sendReceipt(emailRequest);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testSendReceipt_ExceptionThrown() {
        // Arrange
        EmailRequest emailRequest = new EmailRequest("test@example.com", "Body of the email");

        // Mock behavior to throw an exception when sendEmail is called
        doThrow(new RuntimeException("Error sending email")).when(mockEmailService).sendEmail(anyString(), anyString());

        // Act
        ResponseEntity<Void> response = basketController.sendReceipt(emailRequest);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}