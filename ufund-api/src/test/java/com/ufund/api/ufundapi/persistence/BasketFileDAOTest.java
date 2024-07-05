package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;



/**
 * Test the Basket File DAO class
 * 
 * @author Kyle Krebs
 */
@Tag("Model-tier")
public class BasketFileDAOTest {

    BasketFileDAO basketFileDAO;

    FundBasket[] testBaskets;

    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupBasketFileDAO() throws IOException {

    mockObjectMapper = mock(ObjectMapper.class);

    //create test baskets 
    testBaskets = new FundBasket[3];
    testBaskets[0] = new FundBasket(1);
    testBaskets[1] = new FundBasket(2);
    testBaskets[2] = new FundBasket(3);

    Toy toy1 = new Toy(1, "Teddy Bear", 10, 15, "Plush");
    Toy toy2 = new Toy(2, "LEGO Set", 20, 15, "Building Blocks");

    //add toys to test basket
    ArrayList<Toy> toys = new ArrayList<>();
    toys.add(toy1);
    toys.add(toy2);

    testBaskets[0].getBasket().addAll(toys);

    // When the object mapper is supposed to read from the file
    // the mock object mapper will return the basket array above
    when(mockObjectMapper
            .readValue(eq(new File("doesnt_matter.txt")), eq(FundBasket[].class)))
            .thenReturn(testBaskets);

    basketFileDAO = new BasketFileDAO("doesnt_matter.txt", mockObjectMapper);

    }

    @Test
    public void testGetBasket() throws IOException {

        // Invoke
        FundBasket basket = basketFileDAO.getBasket(1);

        // Analzye
        assertEquals(basket,testBaskets[0]);
    }

    @Test
    void testAddNewToyToExistingBasket() throws IOException {
        Toy newToy = new Toy(3, "Optimus Prime", 10, 10, "Action Figure");
        FundBasket result = basketFileDAO.addToy(1, newToy);

        assertTrue(result.getBasket().contains(newToy), "New toy should be added to the basket");
        assertEquals(3, result.getBasket().size(), "basket should have 3 toys after adding the new one");
    }

    @Test
    void testAddExistingToyToBasket() throws IOException {
        // Assume toy1 is already in basket 1
        Toy duplicateToy = new Toy(1, "Teddy Bear", 5, 15, "Plush");
        FundBasket result = basketFileDAO.addToy(1, duplicateToy);

        Toy updatedToy = result.getBasket().stream()
                               .filter(toy -> toy.getId() == 1)
                               .findFirst()
                               .orElse(null);

        assertNotNull(updatedToy, "The existing toy should be found in the basket");
        assertEquals(15, updatedToy.getQuantity(), "Quantity of the existing toy should be updated");
    }

    @Test
    void testAddToyToNonExistentBasket() throws IOException {
        Toy newToy = new Toy(4, "Barbie Doll", 5, 10, "Doll");
        FundBasket result = basketFileDAO.addToy(99, newToy);

        assertNull(result, "Trying to add a toy to a non-existent basket should return null");
    }

    @Test
    public void testCreateBasket() throws IOException {
        // Setup

        FundBasket expectedBasket = new FundBasket(2);

        // Invoke
        FundBasket result = assertDoesNotThrow(() -> basketFileDAO.createBasket(),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        FundBasket actual = basketFileDAO.getBasket(expectedBasket.getId());
        assertEquals(actual.getId(),expectedBasket.getId());
        assertEquals(actual.getBasket(),expectedBasket.getBasket());
    }

    @Test
    public void testRemoveToy() throws IOException {
        // Invoke
        boolean result = assertDoesNotThrow(() -> basketFileDAO.removeToy(1, 1),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,true);
        // We check the internal tree map size against the length
        // of the test basket array - 1 (because of the removal)
        // Because basket attribute of basketFileDAO is package private
        // we can access it directly
        assertEquals(basketFileDAO.getBasket(1).getBasket().size(),testBaskets[0].getBasket().size());
    }

    @Test
    void testRemoveToyFromNonExistentBasket() throws IOException {
        boolean result = basketFileDAO.removeToy(99, 4);

        assertFalse(result, "Trying to remove a toy to a non-existent basket should return null");
    }

    @Test
    public void testgetBasketNotFound() throws IOException {
        // Invoke
        FundBasket basket = basketFileDAO.getBasket(98);

        // Analyze
        assertEquals(basket,null);
    }

    @Test
    public void testRemoveToyNotFound() throws IOException {
        // Invoke
        boolean result = assertDoesNotThrow(() -> basketFileDAO.removeToy(1, 98),
                                                "Unexpected exception thrown");

        // Analyze
        assertEquals(result,false);
        assertEquals(basketFileDAO.getBasket(1).getBasket().size(),testBaskets[0].getBasket().size());
    }

    @Test
    public void testConstructorException() throws IOException {
        // Setup
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        // We want to simulate with a Mock Object Mapper that an
        // exception was raised during JSON object deseerialization
        // into Java objects
        // When the Mock Object Mapper readValue method is called
        // from the HeroFileDAO load method, an IOException is
        // raised
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"),FundBasket[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new BasketFileDAO("doesnt_matter.txt",mockObjectMapper),
                        "IOException not thrown");
    }

    @Test
    public void testSaveException() throws IOException{

        // Setup the mock to throw IOException when writeValue is called
        doThrow(new IOException())
        .when(mockObjectMapper)
        .writeValue(any(File.class), any(ArrayList.class));


        // Call the method that is supposed to trigger the IOException
        // and assert that it throws IOException
        assertThrows(IOException.class,
                    () -> basketFileDAO.createBasket(),
                    "IOException not thrown");
    }

    @Test
    public void testDeleteBasket() throws IOException {
        // Setup
        int basketId = 1;

        // Invoke
        boolean result = basketFileDAO.deleteBasket(basketId);

        // Analyze
        assertTrue(result, "Basket should be deleted");
        assertEquals(2, basketFileDAO.baskets.size(), "Only one basket should remain");
    }

    @Test
    public void testDeleteNonExistentBasket() throws IOException {
        // Setup
        int basketId = 99;

        // Invoke
        boolean result = basketFileDAO.deleteBasket(basketId);

        // Analyze
        assertTrue(!result, "Deleting a non-existent basket should return false");
        assertEquals(3, basketFileDAO.baskets.size(), "No basket should be removed");
    }

    @Test
    public void testGetBaskets() {
        // Invoke
        FundBasket[] result = basketFileDAO.getBaskets();

        // Analyze
        assertEquals(3, result.length, "Expected number of baskets should be returned");
    }

    @Test
    public void testGetBasketsEmpty() {
        // Setup
        basketFileDAO.baskets.clear();

        // Invoke
        FundBasket[] result = basketFileDAO.getBaskets();

        // Analyze
        assertEquals(0, result.length, "No baskets should be returned");
    }

}