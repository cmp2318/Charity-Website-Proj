package com.ufund.api.ufundapi.model;

import java.util.Objects;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Toy {
     
    // Package private for tests (referenced from toy)
    static final String STRING_FORMAT = "Toy [id=%d, name=%s, quantity=%d, cost=%d, type=%s]";

    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("quantity") private int quantity;
    @JsonProperty("cost") private int cost;
    @JsonProperty("type") private String type;

    /**
     * Create a toy with the given id and name
     * @param id The id of the toy
     * @param name The name of the toy
     * @param quantity the amount of toy in stock/needed
     * @param cost the cost of the toy
     * @param type the type of toy eg. Board game, Action Figure, Doll, etc.
     * 
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    public Toy(@JsonProperty("id") int id, @JsonProperty("name") String name,
               @JsonProperty("quantity") int quantity, @JsonProperty("cost") int cost, 
               @JsonProperty("type") String type) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.cost = cost;
        this.type = type;
    }

    /**
     * Retrieves the id of the toy
     * @return The id of the toy
     */
    public int getId() {return id;}

    /**
     * Sets the name of the toy - necessary for JSON object to Java object deserialization
     * @param name The name of the toy
     */
    public void setName(String name) {this.name = name;}

    public void setQuantity(int quantity) {this.quantity = quantity;}

    public void setCost(int cost) {this.cost = cost;}

    public void setType(String type) {this.type = type;}

    /**
     * Retrieves the name of the toy
     * @return The name of the toy
     */
    public String getName() {return name;}

    /**
     * Gets how many toys in stock
     * @return integer value of stock
     */
    public int getQuantity() {return quantity;}

    /**
     * Gets the type of toy
     * @return String representing toy type
     */
    public String getType() {return type;}

    /**
     * Tells user if toy is in stock
     * @return TRUE if in stock, false otherwise
     */
    public boolean inStock() {
        return quantity > 0;
    }

    /**
     * Checks the type of toy
     * @param type String of toy to be checked
     * @return TRUE if type matches, FALSE otherwise
     */
    public boolean typeIs(String type) {
        return this.type.equals(type);
    }

    /**
     * gets the cost of the toy
     * @return cost of toy in dollars
     */
    public int getCost() {
        return cost;
    }

    /**
     * Increases the quantity by a certain amount
     * @param amount to increase the quantity by
     */
    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,id,name, quantity, cost, type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {

        if(o == this) {
            return true;
        }

        if(!(o instanceof Toy)) {
            return false;
        }

        Toy toy = (Toy) o;

        return this.id == toy.id && Objects.equals(this.name, toy.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
