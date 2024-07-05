package com.ufund.api.ufundapi.persistence;

import java.io.IOException;

import com.ufund.api.ufundapi.model.FundBasket;
import com.ufund.api.ufundapi.model.Toy;

public interface BasketDAO {

    /**
     * Retrieves a {@linkplain FundBasket basket} with the given id from the basket
     * 
     * @param id The id of the {@link FundBasket basket} to get
     * 
     * @return a {@link FundBasket basket} object with the matching id
     * <br>
     * null if no {@link FundBasket basket} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    FundBasket getBasket(int id) throws IOException;

    /**
     * Retrieves all {@linkplain FundBasket baskets}
     * 
     * @return An array of {@link FundBasket baskets} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    FundBasket[] getBaskets() throws IOException;

    /**
     * Adds a prexisting toy to the basket, if toy in basket then updates amount of toy
     * 
     * @param toy the toy to add to basket
     * 
     * @param basketId the id of basket to add toy to
     * 
     * @return The updated basket
     * 
     * @throws IOException
     */
    FundBasket addToy(int basketId, Toy toy) throws IOException;

    /**
     * Creates and saves a {@linkplain FundBasket basket}
     * 
     * @param basket {@linkplain FundBasket basket} object to be created and saved
     * <br>
     * The id of the basket object is ignored and a new uniqe id is assigned
     *
     * @return new {@link Toy basket} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    FundBasket createBasket() throws IOException;

    /**
     * Removes a {@linkplain Toy toy} with the given id from a given basket
     * 
     * @param toyId The id of the {@link Toy toy}
     * 
     * @param basketId the id of the {@link FundBasket basket}
     * 
     * @return true if the {@link Toy toy} was removed
     * <br>
     * false if toy with the given id does not exist within the basket or basket doesn't exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean removeToy(int toyId, int basketId) throws IOException;

    /**
     * Removes a {@linkplain FundBasket basket} with the given ID from the system
     * 
     * @param id
     * @return
     * @throws IOException
     */
    boolean deleteBasket(int id) throws IOException;
}