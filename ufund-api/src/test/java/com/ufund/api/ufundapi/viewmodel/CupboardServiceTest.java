package com.ufund.api.ufundapi.viewmodel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ufund.api.ufundapi.model.FundBasket;
import com.ufund.api.ufundapi.model.Toy;
import com.ufund.api.ufundapi.persistence.CupboardFileDAO;

public class CupboardServiceTest {
    private CupboardFileDAO mockCupboardFileDAO;
    private CupboardService CupboardService;
    private BasketService BasketService;

    @BeforeEach
    public void setup() {
        mockCupboardFileDAO = mock(CupboardFileDAO.class);
        BasketService = mock(BasketService.class); 
        CupboardService = new CupboardService(mockCupboardFileDAO, BasketService);
    }

    @Test
    public void testDeleteToy() throws IOException { // ERROR
        // Setup
        int ToyId = 1;
        
        // Mock behavior
        when(mockCupboardFileDAO.deleteToy(ToyId)).thenReturn(true);

        // Invoke and assert
        assertTrue(CupboardService.deleteToy(ToyId));
    }

    @Test
    public void testCreateToy() throws IOException {
        // Setup
        Toy newToy = new Toy(1,"name",0,0,"type");

        // Mock behavior
        when(mockCupboardFileDAO.createToy(newToy)).thenReturn(newToy);

        // Invoke and assert
        assertEquals(newToy, CupboardService.createToy(newToy));
    }

    @Test
    public void testGetCupboard() throws IOException {
        // Setup
        int ToyId = 1;
        Toy newToy = new Toy(ToyId,"name",0,0,"type");

        // Mock behavior
        when(mockCupboardFileDAO.getToy(ToyId)).thenReturn(newToy);

        // Invoke and assert
        assertEquals(newToy, CupboardService.getToyByID(ToyId));
    }

    @Test
    public void testGetEntireCupboard() throws IOException {
        // Setup
        int ToyId = 1;
        Toy newToy = new Toy(ToyId,"name",0,0,"type");
        Toy[] toys = new Toy[1];
        toys[0] = newToy;
        mockCupboardFileDAO.createToy(newToy);
        // Mock behavior
        when(mockCupboardFileDAO.getToys()).thenReturn(toys);

        // Invoke and assert
        assertEquals(toys, CupboardService.getAllToys());
    }
    @Test
    public void testUpdateToy() throws IOException { // ERROR
        // Setup
        int ToyId = 1;
        Toy newToy = new Toy(ToyId,"name",0,0,"type");
        Toy UpdatedToy = new Toy(ToyId,"name",1,0,"type");
        Toy[] toys = new Toy[1];
        toys[0] = newToy;
        mockCupboardFileDAO.createToy(newToy);
        // Mock behavior
        when(mockCupboardFileDAO.updateToy(UpdatedToy)).thenReturn(UpdatedToy);

        // Invoke and assert
        assertEquals(UpdatedToy, CupboardService.updateToy(UpdatedToy));
    }
  
    @Test
    public void testFindToys() throws IOException {
      // Setup
      int ToyId = 1;
      Toy newToy = new Toy(ToyId,"name",0,0,"type");
    
      Toy[] toys = new Toy[1];
      toys[0] = newToy;
      mockCupboardFileDAO.createToy(newToy);
      // Mock behavior
      when(mockCupboardFileDAO.findToys("name")).thenReturn(toys);

      // Invoke and assert
      assertEquals(toys, CupboardService.findToys("name"));
  }

  @Test
    void updateToyAcrossAllBaskets_success() throws IOException {
        
        Toy updatedToy = new Toy(2, "Teddy Bear", 10, 15, "Plush");
        int basketId = 1;
        Toy toy1 = new Toy(1, "Teddy Bear", 1,10,"Plush"); // Initial quantity is 1
        FundBasket initialBasket = new FundBasket(basketId);
        initialBasket.getBasket().add(toy1);
        initialBasket.getBasket().add(updatedToy);
        FundBasket[] allBaskets = {initialBasket};

        when(BasketService.getAllBaskets()).thenReturn(allBaskets);

        CupboardService.updateToyAcrossAllBaskets(updatedToy);

        // Verify the toy was updated in the baskets
        for (FundBasket basket : allBaskets) {
            for (Toy toy : basket.getBasket()) {
                if (toy.getId() == updatedToy.getId()) {
                    assertEquals(updatedToy.getName(), toy.getName());
                    assertEquals(updatedToy.getCost(), toy.getCost());
                    // Other assertions as needed
                }
            }
        }

        verify(BasketService).getAllBaskets();
    }

    @Test
    void removeToyFromAllBaskets_success() throws IOException {
        Toy updatedToy = new Toy(2, "Teddy Bear", 10, 15, "Plush");
        int basketId = 1;
        Toy toy1 = new Toy(1, "Teddy Bear", 1,10,"Plush"); // Initial quantity is 1
        FundBasket initialBasket = new FundBasket(basketId);
        initialBasket.getBasket().add(toy1);
        initialBasket.getBasket().add(updatedToy);
        FundBasket[] allBaskets = {initialBasket};
        int toyIdToRemove = 1;

        when(BasketService.getAllBaskets()).thenReturn(allBaskets);

        CupboardService.removeToyFromAllBaskets(toyIdToRemove);

        // Verify the toy was removed from the baskets
        for (FundBasket basket : allBaskets) {
            assertTrue(basket.getBasket().stream().noneMatch(toy -> toy.getId() == toyIdToRemove));
        }

        verify(BasketService).getAllBaskets();
    }

    

}

