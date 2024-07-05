package com.ufund.api.ufundapi.persistence;

import java.io.IOException;
import java.util.List;

import com.ufund.api.ufundapi.model.User;

public interface UserDAO {

     /**
     * Retrieves all {@linkplain User toys}
     * 
     * @return An array of {@link User user} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    User[] getUsers() throws IOException;

    /**
     * Finds all {@linkplain User toys} whose name contains the given text
     * 
     * @param containsText The text to match against
     * 
     * @return An array of {@link User toys} whose nemes contains the given text, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    User[] findUsers(String containsText) throws IOException;

    /**
     * Retrieves a {@linkplain User user} with the given id
     * 
     * @param id The id of the {@link User user} to get
     * 
     * @return a {@link User user} object with the matching id
     * <br>
     * null if no {@link User user} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    User getUser(int id) throws IOException;

    /**
     * Creates and saves a {@linkplain User user}
     * 
     * @param user {@linkplain User user} object to be created and saved
     * <br>
     * The id of the user object is ignored and a new uniqe id is assigned
     *
     * @return new {@link User user} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    User addUser(User user) throws IOException;

    /**
     * Updates and saves a {@linkplain User user}
     * 
     * @param {@link User user} object to be updated and saved
     * 
     * @return updated {@link User user} if successful, null if
     * {@link User user} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    User updateUser(User user) throws IOException;

    /**
     * Deletes a {@linkplain User user} with the given id
     * 
     * @param id The id of the {@link User user}
     * 
     * @return true if the {@link User user} was deleted
     * <br>
     * false if user with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteUser(int id) throws IOException;

    /**
     * Gets the ID of a user by their name
     * 
     * @param name The name of the user
     * @return The ID of the user if found, or -1 if not found
     */
    int getUserIdByName(String name);

    /**
     * Allows the user to apply for partnership, adds them to applications.json
     * @param userId id of user to add
     * @return true if they are added to applications.json
     * @throws IOException if underlying storage cannot be accessed 
     */
    boolean applyForPartnership(int userId) throws IOException;

    /**
     * Adds a partner moves them from applications -> partners
     * @param userId id of user to move
     * @return true if user moved successfully 
     * @throws IOException if underlying storage cannot be accessed 
     */
    boolean addPartner(int userId) throws IOException;

    /**
     * Gets the list of ids of everyone that has applied 
     * @return a list holding integers representing ids of users
     * @throws IOException if underlying storage cannot be accessed 
     */
    List<Integer> getApplications() throws IOException;

    /**
     * Gets list of all partners from partners.json
     * @return integer list of all ids of users
     * @throws IOException if underlying storage cannot be accessed 
     */
    List<Integer> getPartners() throws IOException;

    /**
     * Removes a user from applications.json
     * @param userId id to remove 
     * @return true is removed correctly
     * @throws IOException if underlying storage cannot be accessed 
     */
    boolean removeApplication(int userId) throws IOException;


}
