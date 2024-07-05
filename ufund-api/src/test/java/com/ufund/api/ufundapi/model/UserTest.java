package com.ufund.api.ufundapi.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testSetName() {
        User user = new User( "Original Name",1);
        user.setName("New Name");
        assertEquals("New Name", user.getName(), "The name should be updated to 'New Name'.");
    }
    @Test
    void testEqualsWithSameIdAndName() {
        User user1 = new User( "Test User",1);
        User user2 = new User( "Test User",1);
        assertTrue(user1.equals(user2), "Users with the same ID and name should be considered equal.");
    }

    @Test
    void testEqualsWithDifferentId() {
        User user1 = new User( "Test User",1);
        User user2 = new User( "Test User",2);
        assertFalse(user1.equals(user2), "Users with different IDs should not be considered equal.");
    }

    @Test
    void testEqualsWithDifferentName() {
        User user1 = new User( "Test User",1);
        User user2 = new User( "Another User",1);
        assertFalse(user1.equals(user2), "Users with the same ID but different names should not be considered equal.");
    }

    @Test
    void testEqualsWithNonUserObject() {
        User user = new User( "Test User",1);
        Object obj = new Object();
        assertFalse(user.equals(obj), "A User should not be equal to a non-User object.");
    }

    @Test
    void testHashCode() {
        User user1 = new User("Test User", 1);
        User user2 = new User("Test User", 1);

        assertEquals(user1.hashCode(), user2.hashCode());
    }

}
