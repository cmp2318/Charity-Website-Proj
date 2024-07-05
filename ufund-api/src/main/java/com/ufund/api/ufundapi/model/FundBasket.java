package com.ufund.api.ufundapi.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class FundBasket { 

    static final String STRING_FORMAT = "Funding Basket: [id=%d, basket=%s]";

    @JsonProperty("id") private int id;

    @JsonProperty("basket") private ArrayList<Toy> basket;

    /**
     * Creates a funding basket with a given ID and initialize an empty funding basket. 
     * @param id The id of the toy
     * @param basket An array of toys 
     * 
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */ 
    public FundBasket(@JsonProperty("id") int id){
        
        this.id = id;

        this.basket = new ArrayList<>();
    }

    /**
     * Gets the id of the Basket
     * @return the id of the basket
     */
    public int getId() {return id;}

    /**
     * Gets the basket of the toys 
     * @return the basket of toys within the funding basket
     */
    public ArrayList<Toy> getBasket() {return basket;}

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,id, basket.toString());
    }
}
