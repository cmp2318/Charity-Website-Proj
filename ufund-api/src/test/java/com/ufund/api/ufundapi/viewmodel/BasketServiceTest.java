package com.ufund.api.ufundapi.viewmodel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ufund.api.ufundapi.model.FundBasket;
import com.ufund.api.ufundapi.model.Toy;
import com.ufund.api.ufundapi.persistence.BasketFileDAO;

public class BasketServiceTest {
    private BasketFileDAO mockBasketFileDAO;
    private BasketService basketService;

    @BeforeEach
    public void setup() {
        mockBasketFileDAO = mock(BasketFileDAO.class);
        basketService = new BasketService(mockBasketFileDAO);
    }

    @Test
    public void testRemoveToy() throws IOException {
        // Setup
        int basketId = 1;
        int toyId = 1;

        // Mock behavior
        when(mockBasketFileDAO.removeToy(basketId, toyId)).thenReturn(true);

        // Invoke and assert
        assertTrue(basketService.removeToy(basketId, toyId));
    }

    @Test
    public void testCreateBasket() throws IOException {
        // Setup
        FundBasket newBasket = new FundBasket(1);

        // Mock behavior
        when(mockBasketFileDAO.createBasket()).thenReturn(newBasket);

        // Invoke and assert
        assertEquals(newBasket, basketService.createBasket());
    }

    @Test
    public void testAddToyBasket() throws IOException {
        // Setup
        int basketId = 1;
        Toy toy = new Toy(1, "Teddy Bear", 10,10,"Plush");
        FundBasket updatedBasket = new FundBasket(basketId);

        // Mock behavior
        when(mockBasketFileDAO.addToy(basketId, toy)).thenAnswer(invocation -> {

            Toy toyToAdd = invocation.getArgument(1);

            updatedBasket.getBasket().add(toyToAdd);

            return updatedBasket;

        });

        // Invoke and assert
        assertEquals(updatedBasket, basketService.addToyBasket(basketId, toy));
    }

    @Test
    public void testGetBasket() throws IOException {
        // Setup
        int basketId = 1;
        FundBasket basket = new FundBasket(basketId);

        // Mock behavior
        when(mockBasketFileDAO.getBasket(basketId)).thenReturn(basket);

        // Invoke and assert
        assertEquals(basket, basketService.getBasket(basketId));
    }

    @Test
    public void testGetAllBaskets() throws IOException {
        // Setup
        FundBasket[] baskets = { new FundBasket(1), new FundBasket(2) };

        // Mock behavior
        when(mockBasketFileDAO.getBaskets()).thenReturn(baskets);

        // Invoke
        FundBasket[] result = basketService.getAllBaskets();

        // Assert
        assertArrayEquals(baskets, result);
    }

    @Test
    public void testDeleteBasket() throws IOException {
        // Setup
        int basketId = 1;

        // Mock behavior
        when(mockBasketFileDAO.deleteBasket(basketId)).thenReturn(true);

        // Invoke
        boolean result = basketService.deleteBasket(basketId);

        // Assert
        assertEquals(true, result);
    }


}