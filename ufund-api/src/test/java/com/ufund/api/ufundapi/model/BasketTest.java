package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for the Basket class
 * 
 * @author Kyle Krebs
 */
@Tag("Model-tier")
public class BasketTest {

    @Test
    public void testBasket() {
        // Setup
        int expected_id = 5;

        // Invoke
        FundBasket basket = new FundBasket(expected_id);

        ArrayList<Toy> expectedToyBasket = new ArrayList<>();

        // Analyze
        assertEquals(expected_id,basket.getId());
        assertEquals(expectedToyBasket.size(),basket.getBasket().size());
    }

    @Test
    public void testGetBasket() {
        // Setup
        int id = 5;

        ArrayList<Toy> expected_basket = new ArrayList<>();
        Toy toy2 = new Toy(10, "Uno", 40, 2, "Card Game");
        expected_basket.add(toy2);

        // Invoke
        FundBasket basket = new FundBasket(id);
        basket.getBasket().add(toy2);

        // Analyze
        assertEquals(expected_basket,basket.getBasket());
    }

    @Test
    public void testToString() {
        // Setup
        int id = 5;

        Toy toy1 = new Toy(10, "Uno", 40, 2, "Card Game");
        ArrayList<Toy> toyBasket = new ArrayList<>();
        toyBasket.add(toy1);

        String expected_string = String.format(FundBasket.STRING_FORMAT,id,toyBasket.toString());

        // Invoke
        FundBasket basket = new FundBasket(id);
        basket.getBasket().addAll(toyBasket);
        String actual_string = basket.toString();

        // Analyze
        assertEquals(expected_string,actual_string);
    }

}