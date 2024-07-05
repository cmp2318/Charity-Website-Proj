package com.ufund.api.ufundapi.persistence;

import java.io.IOException;

import com.ufund.api.ufundapi.model.Toy;

public interface CupboardDAO {

     /**
     * Retrieves all {@linkplain Toy toys}
     * 
     * @return An array of {@link Toy toy} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Toy[] getToys() throws IOException;

    /**
     * Finds all {@linkplain Toy toys} whose name contains the given text
     * 
     * @param containsText The text to match against
     * 
     * @return An array of {@link Toy toys} whose nemes contains the given text, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Toy[] findToys(String containsText) throws IOException;

    /**
     * Retrieves a {@linkplain Toy toy} with the given id
     * 
     * @param id The id of the {@link Toy toy} to get
     * 
     * @return a {@link Toy toy} object with the matching id
     * <br>
     * null if no {@link Toy toy} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Toy getToy(int id) throws IOException;

    /**
     * Creates and saves a {@linkplain Toy toy}
     * 
     * @param toy {@linkplain Toy toy} object to be created and saved
     * <br>
     * The id of the toy object is ignored and a new uniqe id is assigned
     *
     * @return new {@link Toy toy} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    Toy createToy(Toy toy) throws IOException;

    /**
     * Updates and saves a {@linkplain Toy toy}
     * 
     * @param {@link Toy toy} object to be updated and saved
     * 
     * @return updated {@link Toy toy} if successful, null if
     * {@link Toy toy} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    Toy updateToy(Toy toy) throws IOException;

    /**
     * Deletes a {@linkplain Toy toy} with the given id
     * 
     * @param id The id of the {@link Toy toy}
     * 
     * @return true if the {@link Toy toy} was deleted
     * <br>
     * false if toy with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteToy(int id) throws IOException;
}
