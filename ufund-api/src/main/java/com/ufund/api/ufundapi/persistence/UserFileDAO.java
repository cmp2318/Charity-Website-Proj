package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import com.ufund.api.ufundapi.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserFileDAO implements UserDAO {

    //Uses data file 

    Map<Integer,User> users;   // Provides a local cache of the user objects
                                // so that we don't need to read from the file
                                // each time
    private ObjectMapper objectMapper;  // Provides conversion between User
                                        // objects and JSON text format written
                                        // to the file
    private static int nextId;  // The next Id to assign to a new user
    private String filename;    // Filename to read from and write to
    private String applicationsFilename;
    private String partnersFilename; 

    /**
     * Creates a User File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public UserFileDAO(@Value("${users.file}") String filename, @Value("${applications.file}") String applicationsFilename,
     @Value("${partners.file}") String partnersFilename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.applicationsFilename = applicationsFilename;
        this.partnersFilename = partnersFilename;
        this.objectMapper = objectMapper;
        load();  // load the toys from the file
    }

    /**
     * Generates the next id for a new {@linkplain User }
     * 
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Generates an array of {@linkplain User toys} from the tree map
     * 
     * @return  The array of {@link User toys}, may be empty
     */
    private User[] getUsersArray() {
        return getUsersArray(null);
    }

    /**
     * Generates an array of {@linkplain User toys} from the tree map for any
     * {@linkplain User toys} that contains the text specified by containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain User toys}
     * in the tree map
     * 
     * @return  The array of {@link User toys}, may be empty
     */
    private User[] getUsersArray(String containsText) { // if containsText == null, no filter
        ArrayList<User> userArrayList = new ArrayList<>();

        for (User user : users.values()) {
            if (containsText == null || user.getName().contains(containsText)) {
                userArrayList.add(user);
            }
        }

        User[] userArray = new User[userArrayList.size()];
        userArrayList.toArray(userArray);
        return userArray;
    }

    /**
     * Saves the {@linkplain User toys} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link User toys} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        User[] userArray = getUsersArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),userArray);
        return true;
    }

    /**
     * Loads {@linkplain User toys} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        users = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of toys
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        User[] userArray = objectMapper.readValue(new File(filename),User[].class);

        // Add each user to the tree map and keep track of the greatest id
        for (User user : userArray) {
            users.put(user.getId(),user);
            if (user.getId() > nextId)
                nextId = user.getId();
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public User[] getUsers() {
        synchronized(users) {
            return getUsersArray();
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public User[] findUsers(String containsText) {
        synchronized(users) {
            return getUsersArray(containsText);
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public User getUser(int id) {
        synchronized(users) {
            if (users.containsKey(id)){

                return users.get(id);
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
    public User addUser(User user) throws IOException {
        synchronized(users) {
            // We create a new user object because the id field is immutable
            // and we need to assign the next unique id
            User newUser = new User(user.getName(),nextId());
            users.put(newUser.getId(),newUser);
            save(); // may throw an IOException


            return newUser;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public User updateUser(User user) throws IOException {
        synchronized(users) {
            if (users.containsKey(user.getId()) == false)
                return null;  // user does not exist

            users.put(user.getId(),user);
            save(); // may throw an IOException
            return user;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean deleteUser(int id) throws IOException {
        synchronized(users) {
            if (users.containsKey(id)) {
                users.remove(id);
                return save();
            }
            else
                return false;
        }
    }


    /**
     * Gets the ID of a user by their name
     * 
     * @param name The name of the user
     * @return The ID of the user if found, or -1 if not found
     */
    public int getUserIdByName(String name) {
        synchronized (users) {
            for (User user : users.values()) {
                if (user.getName().equalsIgnoreCase(name)) {
                    return user.getId();
                }
            }
            return -1; // User not found
        }
    }

    
    /**
    ** {@inheritDoc}
     */
    public boolean addPartner(int userId) throws IOException {
        synchronized (this) {
            File partnersFile = new File(partnersFilename);
    
            // Initialize an empty list for user IDs
            List<Integer> partners = new ArrayList<>();
    
            // Check if the applications file exists to read the current applications
            if (partnersFile.exists()) {
                // Use objectMapper to convert JSON array to List
                partners = objectMapper.readValue(partnersFile, new TypeReference<List<Integer>>(){});
            }
    
            // Add the user ID to the list regardless of its presence
            partners.add(userId);
    
            // Write the updated list of applications back to the file
            objectMapper.writeValue(partnersFile, partners);
    
            return true; // Indicate the user's application was added
        }
    }

    /**
    ** {@inheritDoc}
     */
    public boolean applyForPartnership(int userId) throws IOException {
        synchronized (this) {
            File applicationsFile = new File(applicationsFilename);

            // Initialize an empty list for user IDs
            List<Integer> applications = new ArrayList<>();

            // Check if the applications file exists to read the current applications
            if (applicationsFile.exists()) {
                // Use objectMapper to convert JSON array to List
                applications = objectMapper.readValue(applicationsFile, new TypeReference<List<Integer>>(){});
            }

            // Add the user ID to the list regardless of its presence
            applications.add(userId);

            // Write the updated list of applications back to the file
            objectMapper.writeValue(applicationsFile, applications);

            return true; // Indicate the user's application was added
        }
    }   

    /**
    ** {@inheritDoc}
     */
    public List<Integer> getApplications() throws IOException {
        File applicationsFile = new File(applicationsFilename);
        if (applicationsFile.exists()) {
            // Return the list of application IDs
            return objectMapper.readValue(applicationsFile, new TypeReference<List<Integer>>(){});
        } else {
            // Return an empty list if the file doesn't exist
            return new ArrayList<>();
        }
    }

    /**
    ** {@inheritDoc}
     */
    public List<Integer> getPartners() throws IOException {
        File partnersFile = new File(partnersFilename);
        if (partnersFile.exists()) {
            return objectMapper.readValue(partnersFile, new TypeReference<List<Integer>>(){});
        } else {
            return new ArrayList<>();
        }
    }

    /**
    ** {@inheritDoc}
     */
    public boolean removeApplication(int userId) throws IOException {
        synchronized (this) {
            File applicationsFile = new File(applicationsFilename);
            List<Integer> applications = new ArrayList<>();
            if (applicationsFile.exists()) {
                applications = objectMapper.readValue(applicationsFile, new TypeReference<List<Integer>>(){});
            }
            boolean removed = applications.remove(Integer.valueOf(userId));
            if (removed) {
                objectMapper.writeValue(applicationsFile, applications);
            }
            return removed;
        }
    }

}
