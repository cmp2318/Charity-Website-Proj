package com.ufund.api.ufundapi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
public class ToyTest {
    private Toy toy;

    @BeforeEach
    public void setup() {
        toy = new Toy(1, "Teddy Bear", 10, 15, "Plush");
    }

    @Test
    public void testGetId() {
        assertEquals(1, toy.getId(), "The ID should match the one set in the constructor");
    }

    @Test
    public void testGetName() {
        assertEquals("Teddy Bear", toy.getName(), "The name should match the one set in the constructor");
    }

    @Test
    public void testSetName() {
        toy.setName("Doll");
        assertEquals("Doll", toy.getName(), "The name should be updated by the setName method");
    }

    @Test
    public void testGetQuantity() {
        assertEquals(10, toy.getQuantity(), "The quantity should match the one set in the constructor");
    }

    @Test
    public void testIncreaseQuantity() {
        toy.increaseQuantity(5);
        assertEquals(15, toy.getQuantity(), "The quantity shoul be increased by the amount specified");
    }

    @Test
    public void testGetCost() {
        assertEquals(15, toy.getCost(), "The cost : match the one set in the constructor");
    }

    @Test
    public void testGetType() {
        assertEquals("Plush", toy.getType(), "The type : match the one set in the constructor");
    }

    @Test
    public void testInStock() {
        assertTrue(toy.inStock(), "inStock : return true if the quantity is greater than 0");
    }

    @Test
    public void testTypeIs() {
        assertTrue(toy.typeIs("Plush"), "typeIs : return true if the type matches the one set in the constructor");
    }

    @Test
    public void testToString() {
        String expected = "Toy [id=1, name=Teddy Bear, quantity=10, cost=15, type=Plush]";
        assertEquals(expected, toy.toString(), "toString : return the string representation matching the format specified");
    }

    @Test
    public void testEquals() {
        Toy sameToy = new Toy(1, "Teddy Bear", 10, 15, "Plush");
        Toy differentToy = new Toy(2, "Robot", 5, 25, "Electronic");
        assertEquals(toy, sameToy, "equals : return true for two toys with the same ID and name");
        assertNotEquals(toy, differentToy, "equals : return false for toys with different IDs or names");
    }
    
    @Test
    void testHashCode() {
        Toy toy1 = new Toy(1, "Action Figure", 2, 10, "Red");
        Toy toy2 = new Toy(1, "Action Figure", 2, 10, "Red");

        assertEquals(toy1.hashCode(), toy2.hashCode(), "Hash codes should be equal for equal objects.");
    }
    

}
