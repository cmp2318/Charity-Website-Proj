package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Toy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CupboardFileDAO implements CupboardDAO {

    //Uses data file

    Map<Integer,Toy> toys;   // Provides a local cache of the toy objects
                                // so that we don't need to read from the file
                                // each time
    private ObjectMapper objectMapper;  // Provides conversion between Toy
                                        // objects and JSON text format written
                                        // to the file
    private static int nextId;  // The next Id to assign to a new toy
    private String filename;    // Filename to read from and write to

    /**
     * Creates a Toy File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public CupboardFileDAO(@Value("${toys.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the toys from the file
    }

    /**
     * Generates the next id for a new {@linkplain Toy }
     * 
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Generates an array of {@linkplain Toy toys} from the tree map
     * 
     * @return  The array of {@link Toy toys}, may be empty
     */
    private Toy[] getToysArray() {
        return getToysArray(null);
    }

    /**
     * Generates an array of {@linkplain Toy toys} from the tree map for any
     * {@linkplain Toy toys} that contains the text specified by containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain Toy toys}
     * in the tree map
     * 
     * @return  The array of {@link Toy toys}, may be empty
     */
    private Toy[] getToysArray(String containsText) { // if containsText == null, no filter
        ArrayList<Toy> toyArrayList = new ArrayList<>();

        for (Toy toy : toys.values()) {
            if (containsText == null || toy.getName().contains(containsText)) {
                toyArrayList.add(toy);
            }
        }

        Toy[] toyArray = new Toy[toyArrayList.size()];
        toyArrayList.toArray(toyArray);
        return toyArray;
    }

    /**
     * Saves the {@linkplain Toy toys} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link Toy toys} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        Toy[] toyArray = getToysArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),toyArray);
        return true;
    }

    /**
     * Loads {@linkplain Toy toys} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        toys = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of toys
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        Toy[] toyArray = objectMapper.readValue(new File(filename),Toy[].class);

        // Add each toy to the tree map and keep track of the greatest id
        for (Toy toy : toyArray) {
            toys.put(toy.getId(),toy);
            if (toy.getId() > nextId)
                nextId = toy.getId();
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Toy[] getToys() {
        synchronized(toys) {
            return getToysArray();
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Toy[] findToys(String containsText) {
        synchronized(toys) {
            return getToysArray(containsText);
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Toy getToy(int id) {
        synchronized(toys) {
            if (toys.containsKey(id)){

                return toys.get(id);
            }
            else{
                return null;
            }
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Toy createToy(Toy toy) throws IOException {
        synchronized(toys) {
            // We create a new toy object because the id field is immutable
            // and we need to assign the next unique id
            Toy newToy = new Toy(nextId(),toy.getName(), toy.getQuantity(), 
                                 toy.getCost(), toy.getType());
            toys.put(newToy.getId(),newToy);
            save(); // may throw an IOException
            return newToy;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Toy updateToy(Toy toy) throws IOException {
        synchronized(toys) {
            if (toys.containsKey(toy.getId()) == false)
                return null;  // toy does not exist

            toys.put(toy.getId(),toy);
            save(); // may throw an IOException
            return toy;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean deleteToy(int id) throws IOException {
        synchronized(toys) {
            if (toys.containsKey(id)) {
                toys.remove(id);
                return save();
            }
            else
                return false;
        }
    }



}
