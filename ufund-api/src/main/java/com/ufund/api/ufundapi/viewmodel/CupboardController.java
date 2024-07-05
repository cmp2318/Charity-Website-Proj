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

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ufund.api.ufundapi.model.Toy;


@RestController
@RequestMapping("toys")
public class CupboardController {
    private static final Logger LOG = Logger.getLogger(CupboardController.class.getName());
    private CupboardService cupboardService;

    public CupboardController(CupboardService cupboardService) {
        this.cupboardService = cupboardService;
    }
    //TO DO: make the cupboard controller


    /**
     * Responds to the GET request for all {@linkplain Toy toys} in the cupboard
     * 
     * @return ResponseEntity with array of {@link Toy toy} objects and
     * HTTP status of OK
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<Toy[]> getAllToys() {
        LOG.info("GET /toys");
        try {
            Toy[] toys = cupboardService.getAllToys();
            return new ResponseEntity<>(toys, HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error getting all toys: " + e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    /**
     * Responds to the GET request for a {@linkplain Toy toy} for the given id
     * 
     * @param id The id used to locate the {@link Toy toy}
     * 
     * @return ResponseEntity with {@link Toy toy} object and HTTP status of OK if found
     * ResponseEntity with HTTP status of NOT_FOUND if not found
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Toy> getToy(@PathVariable int id) {
        LOG.info("GET /toys/" + id);

        try {
            Toy toy = cupboardService.getToyByID(id);
        if(toy == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Toy>(toy,HttpStatus.OK);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error getting toy: " + e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Creates a new toy in the cupboard
     *
     * @param toy The {@link Toy toy} object to create
     * @return ResponseEntity with created {@link Toy toy} object and HTTP status of CREATED
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<Toy> createToy(@RequestBody Toy toy) {
        LOG.info("POST /toys " + toy);

        try {

            //check for a duplicate named toy
            Toy[] toys = cupboardService.findToys(toy.getName());

            for (Toy nameToy : toys) { 
                

                if(nameToy.getName().equals(toy.getName())) {
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                }
            }

            if(toy.getQuantity() <= 0 || toy.getCost() < 0){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            Toy createdToy = cupboardService.createToy(toy);

            

            return new ResponseEntity<>(createdToy, HttpStatus.CREATED);
            
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error creating toy: " + e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

 
    /**
     * Deletes a toy from the cupboard with the specified ID.
     *
     * @param id The ID of the toy to delete
     * @return ResponseEntity with HTTP status of NO_CONTENT if the toy was deleted successfully
     * ResponseEntity with HTTP status of NOT_FOUND if the toy with the given ID does not exist
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Toy> deleteToy(@PathVariable int id) {
        LOG.info("DELETE /toys/" + id);

        try {
            boolean deleted = cupboardService.deleteToy(id);
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error deleting toy: " + e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Toy toys} whose name contains
     * the text in name
     * 
     * @param name The name parameter which contains the text used to find the {@link Toy toys}
     * 
     * @return ResponseEntity with array of {@link Toy toy} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * <p>
     * Example: Find all toys that contain the text "ma"
     * GET http://localhost:8080/toys/?name=ma
     */
    
    @GetMapping("/")
    public ResponseEntity<Toy[]> searchToys(@RequestParam String name) {
        LOG.info("GET /toys/?name=" + name);

        try {

            Toy[] toys = cupboardService.findToys(name);

            return new ResponseEntity<Toy[]>(toys, HttpStatus.OK);
            
        } catch (IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the {@linkplain Toy toy} with the provided {@linkplain Toy toy} object, if it exists
     * 
     * @param hero The {@link Toy toy} to update
     * 
     * @return ResponseEntity with updated {@link Toy hero} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<Toy> updateToy(@RequestBody Toy toy) {
        LOG.info("PUT /toys " + toy);

        try {
            

            Toy updatedToy = cupboardService.updateToy(toy);
            if (updatedToy != null) {
                if(toy.getQuantity() == 0){
                    this.cupboardService.deleteToy(toy.getId());
                }
                if(toy.getCost() < 0 || toy.getQuantity() < 0){
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
                return new ResponseEntity<>(updatedToy, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error updating toy: " + e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}