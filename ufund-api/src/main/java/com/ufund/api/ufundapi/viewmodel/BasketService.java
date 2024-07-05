package com.ufund.api.ufundapi.viewmodel;

import org.springframework.stereotype.Service;

import java.io.IOException;

import com.ufund.api.ufundapi.model.Toy;
import com.ufund.api.ufundapi.persistence.BasketDAO;
import com.ufund.api.ufundapi.persistence.BasketFileDAO;
import com.ufund.api.ufundapi.model.FundBasket;

//connects Basket Controller to Basket DAO
@Service
public class BasketService {

    private BasketFileDAO basketFileDAO;

    /**
   * Service Class that acts as middle man for CRUD operations
   * @Autowired
   * @param basketFileDAO The {@link BasketDAO basket Data Access Object} to perform CRUD operations
   */
  public BasketService(BasketFileDAO basketFileDAO){ // we need to implement a service in the 
    this.basketFileDAO = basketFileDAO;
  }

  /**
   * Removes the selected toy from the selected basket
   * @param basketId
   * @param toyId
   * @return true if succesfully deleted, false otherwise
   * @throws IOException
   */
  public boolean removeToy(int basketId, int toyId) throws IOException{

    return basketFileDAO.removeToy(basketId, toyId);

  }

  /**
   * Creates a basket with a new automatic id and empty basket
   * @return the newly created basket
   * @throws IOException
   */
  public FundBasket createBasket() throws IOException {

    return basketFileDAO.createBasket();

  }

  /**
   * Adds a toy to the basket, uses DAO method to increase quantity if needed
   * @param basketId the id of the basket
   * @param toy the id of the toy
   * @return returns the updated basket, null if basket doesn't exist
   * @throws IOException
   */
  public FundBasket addToyBasket(int basketId, Toy toy) throws IOException {

    return basketFileDAO.addToy(basketId, toy);

  }

  /**
   * Gets a basket with a given basket id
   * @param id the id of the basket
   * @return basket if exists, null otherwise
   * @throws IOException
   */
  public FundBasket getBasket(int id) throws IOException {

    return basketFileDAO.getBasket(id);
  }

  /**
   * Gets all baskets in the system
   * @return array of {@link FundBasket basket}
   * @throws IOException
   */
  public FundBasket[] getAllBaskets() throws IOException {
    return basketFileDAO.getBaskets();
  }

  /**
   * Deletes basket specified by ID
   * @param id
   * @return true if deleted
   * @throws IOException
   */
  public boolean deleteBasket(int id) throws IOException {
    return basketFileDAO.deleteBasket(id); 
  } 
}
