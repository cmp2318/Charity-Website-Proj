package com.ufund.api.ufundapi.viewmodel;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.ufund.api.ufundapi.model.User;


@RestController
@RequestMapping("users")
public class UserController {
    private static final Logger LOG = Logger.getLogger(UserController.class.getName());
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    //TO DO: make the cupboard controller

    @GetMapping("/id/{username}")
    public ResponseEntity<Integer> getUserIdByName(@PathVariable String username) {
        LOG.info("GET /users/id/" + username);
        Integer userId = userService.getUserIdByName(username);

        if (userId == -1){
            // Instead of returning HttpStatus.NOT_FOUND, it returns HttpStatus.OK with a body of -1
            // This indicates to the frontend that the user does not exist and a new one should be created
            return new ResponseEntity<>(-1, HttpStatus.OK);
        }
        
        return new ResponseEntity<>(userId, HttpStatus.OK);
    
    }

    /**
     * Responds to the GET request for all {@linkplain User toys} in the cupboard
     * 
     * @return ResponseEntity with array of {@link User user} objects and
     * HTTP status of OK
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<User[]> getAllUsers() {
        LOG.info("GET /users");
        try {
            User[] users = userService.getUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error getting all users: " + e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    /**
     * Responds to the GET request for a {@linkplain User user} for the given id
     * 
     * @param id The id used to locate the {@link User user}
     * 
     * @return ResponseEntity with {@link User user} object and HTTP status of OK if found
     * ResponseEntity with HTTP status of NOT_FOUND if not found
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id) {
        LOG.info("GET /users/" + id);

        try {
            User user = userService.getUserByID(id);
        if(user == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(user,HttpStatus.OK);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error getting user: " + e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Creates a new user in the cupboard
     *
     * @param user The {@link User user} object to create
     * @return ResponseEntity with created {@link User user} object and HTTP status of CREATED
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        LOG.info("POST /users " + user);

        try {

            //check for a duplicate named user
            User[] users = userService.findUsers(user.getName());

            for (User nameUser : users) { 

                if(nameUser.getName().equals(user.getName())) {
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                }
            }

            User createdUser = userService.addUser(user);

            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
            
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error creating user: " + e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

 
    /**
     * Deletes a user from the cupboard with the specified ID.
     *
     * @param id The ID of the user to delete
     * @return ResponseEntity with HTTP status of NO_CONTENT if the user was deleted successfully
     * ResponseEntity with HTTP status of NOT_FOUND if the user with the given ID does not exist
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable int id) {
        LOG.info("DELETE /users/" + id);

        try {
            boolean deleted = userService.deleteUser(id);
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error deleting user: " + e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain User toys} whose name contains
     * the text in name
     * 
     * @param name The name parameter which contains the text used to find the {@link User toys}
     * 
     * @return ResponseEntity with array of {@link User user} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * <p>
     * Example: Find all toys that contain the text "ma"
     * GET http://localhost:8080/toys/?name=ma
     */
    
    @GetMapping("/")
    public ResponseEntity<User[]> searchUsers(@RequestParam String name) {
        LOG.info("GET /users/?name=" + name);

        try {

            User[] users = userService.findUsers(name);

            return new ResponseEntity<User[]>(users, HttpStatus.OK);
            
        } catch (IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the {@linkplain User user} with the provided {@linkplain User user} object, if it exists
     * 
     * @param hero The {@link User user} to update
     * 
     * @return ResponseEntity with updated {@link User hero} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        LOG.info("PUT /users " + user);

        try {
            User updatedUser = userService.updateUser(user);
            if (updatedUser != null) {
                return new ResponseEntity<>(updatedUser, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error updating usr: " + e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}/makePartner")
    public ResponseEntity<Void> makeUserPartner(@PathVariable int id) {
        try {
            boolean success = userService.makeUserPartner(id);
            if (success) {
                boolean applicationRemoved = userService.removeApplication(id);
                if (applicationRemoved) {
                    LOG.info("Application removed successfully for user ID: " + id);
                } else {
                    LOG.warning("Failed to remove application for user ID: " + id);
                }
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error: " + e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{id}/apply-partnership")
    public ResponseEntity<Void> applyForPartnership(@PathVariable int id) {
        try {
            // Retrieve current applications to check if the user has already applied
            List<Integer> currentApplications = userService.getApplications();
            
            // Check if the user has already applied
            if (currentApplications.contains(id)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            // If not, proceed to add their application
            boolean success = userService.applyForPartnership(id);
            if (success) {
                return new ResponseEntity<>(HttpStatus.OK); // Successfully applied
            } else {
                // This path might be redundant given the current logic, but it's here for completeness
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error applying for partnership: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    

    @GetMapping("/applications")
    public ResponseEntity<List<Integer>> getApplications() {
        try {
            List<Integer> applications = userService.getApplications();
            return ResponseEntity.ok(applications);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error retrieving applications: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    

    @GetMapping("/partners")
    public ResponseEntity<List<Integer>> getPartners() {
        try {
            List<Integer> partners = userService.getPartners();
            return ResponseEntity.ok(partners);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error retrieving partners: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}