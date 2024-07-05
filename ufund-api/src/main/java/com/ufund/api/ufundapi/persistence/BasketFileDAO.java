package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.FundBasket;
import com.ufund.api.ufundapi.model.Toy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BasketFileDAO implements BasketDAO {

    private static final Logger LOG = Logger.getLogger(BasketFileDAO.class.getName());

    Map<Integer,FundBasket> baskets;   // Provides a local cache of the basket objects
                                // so that we don't need to read from the file
                                // each time
    private ObjectMapper objectMapper;  // Provides conversion between basket
                                        // objects and JSON text format written
                                        // to the file
    private static int nextId;  // The next Id to assign to a new basket
    private String filename;    // Filename to read from and write to

    /**
     * Creates a Basket File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public BasketFileDAO(@Value("${baskets.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the basket from the file
    }

    /**
     * Generates the next id for a new {@linkplain FundBasket }
     * 
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Loads {@linkplain FundBasket baskets} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        baskets = new TreeMap<>();
    nextId = 0;

    FundBasket[] basketArray = objectMapper.readValue(new File(filename), FundBasket[].class);
        if (basketArray == null) {
            LOG.warning("No baskets were loaded from the file.");
            return false;
        }

        LOG.info(basketArray.length + " baskets loaded.");

        for (FundBasket basket : basketArray) {
            baskets.put(basket.getId(), basket);
            if (basket.getId() > nextId) {
                nextId = basket.getId();
            }
        }
        nextId++;
        return true;
    }

     /**
     * Saves the {@linkplain FundBasket basket} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link FundBasket basket} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        
        ArrayList<FundBasket> basketsArray = new ArrayList<>(baskets.values());

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename), basketsArray);
        return true;
    }


    /**
    ** {@inheritDoc}
     */
    @Override
    public FundBasket[] getBaskets() {
        synchronized(baskets) {

        ArrayList<FundBasket> basketArrayList = new ArrayList<>();

        for (FundBasket basket : baskets.values()) {
            basketArrayList.add(basket);
        }

        FundBasket[] basketArray = new FundBasket[basketArrayList.size()];
        basketArrayList.toArray(basketArray);
        return basketArray;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FundBasket getBasket(int id) throws IOException {
        
        synchronized(baskets){

            if(baskets.containsKey(id)){

                return baskets.get(id);
            }

            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FundBasket addToy(int basketId, Toy toy) throws IOException {
        
        synchronized (baskets) {
            
            //check if basket exists
            if(baskets.containsKey(basketId)) {

                FundBasket basket = baskets.get(basketId);

                for(Toy basketToy:basket.getBasket()) {
                    
                    //check if the toy exists within the basket already
                    if(basketToy.equals(toy)) {
                        
                        //update the quantity
                        basketToy.increaseQuantity(toy.getQuantity());

                        save();

                        return basket;
                    }
                }

                //toy not in basket, add it
                basket.getBasket().add(toy);

                save();

                return basket;
            }
            //basket doesn't exist
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FundBasket createBasket() throws IOException {
        
        synchronized(baskets) {

            //make new cart
            FundBasket newBasket = new FundBasket(nextId());

            baskets.put(newBasket.getId(), newBasket);
            
            //save the cart
            save();

            return newBasket;

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeToy(int basketId, int toyId) throws IOException {
        
        synchronized (baskets) {

            //make sure that basket exists
            if(baskets.containsKey(basketId)) {

                //get the basket and look through the basket for the toy
                FundBasket basket = baskets.get(basketId);
                
                ArrayList<Toy> toyBasket = basket.getBasket();

                for(Toy toy: toyBasket){
                    
                    //check that toy matches
                    if(toy.getId()==(toyId)){

                        toyBasket.remove(toy);

                        save();

                        return true;
                    }
                }
                return false;
            }
            return false;
        }
    }


    /**
 * Deletes a {@linkplain FundBasket basket} based on its ID
 * 
 * @param basketId The ID of the basket to be deleted
 * @return true if the basket was successfully deleted, false otherwise
 * 
 * @throws IOException when file cannot be accessed or written to
 */
public boolean deleteBasket(int basketId) throws IOException {
    synchronized (baskets) {
        if (baskets.containsKey(basketId)) {
            baskets.remove(basketId);

            save();

            return true;
        }

        return false;
    }
}
}