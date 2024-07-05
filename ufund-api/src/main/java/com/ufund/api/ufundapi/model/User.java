package com.ufund.api.ufundapi.model;

import java.util.Objects;


import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    static final String STRING_FORMAT = "User [name=%s, id=%d]";

    @JsonProperty("name") private String name;
    @JsonProperty("id") private int id;

    private boolean isPartner;

    /**
     * Create a user with the given id and name
     * @param name The name of the user
     * @param id The id of the user
     * 
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    public User(@JsonProperty("name") String name, @JsonProperty("id") int id) {
        this.name = name;
        this.id = id;
        this.isPartner = false;

    }

    public int getId() {return id;}

    public void setPartner(boolean isPartner) {this.isPartner = isPartner;}

    /**
     * Sets the name of the user - necessary for JSON object to Java object deserialization
     * @param name The name of the user
     */
    public void setName(String name) {this.name = name;}

    /**
     * Retrieves the name of the user
     * @return The name of the user
     */
    public String getName() {return name;}



    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,name,id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {

        if(o == this) {
            return true;
        }

        if(!(o instanceof User)) {
            return false;
        }

        User user = (User) o;

        return this.id == user.id &&  
        Objects.equals(this.name, user.name);
    }
    

    public void put(int id2, User user) {
        throw new UnsupportedOperationException("Unimplemented method 'put'");
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}
