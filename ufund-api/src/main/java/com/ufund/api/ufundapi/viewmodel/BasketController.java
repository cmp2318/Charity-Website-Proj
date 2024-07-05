package com.ufund.api.ufundapi.viewmodel;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ufund.api.ufundapi.model.EmailRequest;
import com.ufund.api.ufundapi.model.FundBasket;
import com.ufund.api.ufundapi.model.Toy;


@RestController 
@RequestMapping("baskets")
public class BasketController {

    private static final Logger LOG = Logger.getLogger(BasketController.class.getName());

    private BasketService basketService;

    private EmailService emailService;

    public BasketController(BasketService basketService, EmailService emailService) {
        this.basketService = basketService;
        this.emailService = emailService;
    }

    @GetMapping("")
    public ResponseEntity<FundBasket[]> getAllBaskets() {
        LOG.info("GET /baskets");
        try {
            FundBasket[] baskets = basketService.getAllBaskets();
            return new ResponseEntity<>(baskets, HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error getting all baskets: " + e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

     /**
     * Responds to the GET request for a {@linkplain FundBasket basket} for the given id
     * 
     * @param id The id used to locate the {@link FundBasket basket}
     * 
     * @return ResponseEntity with {@link FundBasket basket} object and HTTP status of OK if found
     * ResponseEntity with HTTP status of NOT_FOUND if not found
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<FundBasket> getBasket(@PathVariable int id) {
        LOG.info("GET /baskets/" + id);
        
        try {
            FundBasket basket = basketService.getBasket(id);

            if(basket == null){

                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<FundBasket>(basket,HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error getting basket: " + e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a new basket in the cupboard
     *
     * @param basket The {@link FundBasket basket} object to create
     * @return ResponseEntity with created {@link FundBasket basket} object and HTTP status of CREATED
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<FundBasket> createBasket() {
        LOG.info("POST /baskets ");

        try {
            
            FundBasket newBasket = basketService.createBasket();

            return new ResponseEntity<>(newBasket,HttpStatus.CREATED);

        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error creating basket: " + e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Adds a {@linkplain Toy toy} to the provided {@linkplain FundBasket basket} object, if it exists
     * 
     * @param basketId The id to locate the {@link FundBasket basket} 
     * 
     * @param toy the {@link Toy toy} to add to the basket
     * 
     * @return ResponseEntity with updated {@link FundBasket basket} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("/{basketId}")
    public ResponseEntity<FundBasket> addToy(@PathVariable int basketId, @RequestBody Toy toy) {
        LOG.info("POST /baskets" + basketId + "/"+ toy);

        try {

            FundBasket updatedBasket = basketService.addToyBasket(basketId, toy);

            if (updatedBasket != null) {
                return new ResponseEntity<>(updatedBasket, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error adding toy to basket: " + e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a toy from a basket
     * @param basketId the id of basket to remove toy from
     * @param toyId the id of toy to remove from basket
     * @return ResponseEntity with HTTP status of OK if the toy was deleted successfully
     * ResponseEntity with HTTP status of NOT_FOUND if the toy with the given ID does not exist
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{basketId}/toys/{toyId}")
    public ResponseEntity<FundBasket> removeToy(@PathVariable int basketId, @PathVariable int toyId) {
        LOG.info("DELETE /baskets/" + basketId +  "/toys/" + toyId);

        try {
            boolean deleted = basketService.removeToy(basketId, toyId);
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
     * Deletes a basket with the specified ID.
     *
     * @param id The ID of the basket to delete
     * @return ResponseEntity with HTTP status of NO_CONTENT if the basket was deleted successfully
     * ResponseEntity with HTTP status of NOT_FOUND if the basket with the given ID does not exist
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<FundBasket> deleteBasket(@PathVariable int id) {
        LOG.info("DELETE /baskets/" + id);

        try {
            boolean deleted = basketService.deleteBasket(id);
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error deleting basket: " + e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/send-email")
    public ResponseEntity<Void> sendReceipt(@RequestBody EmailRequest emailRequest){

        try {
            
            //call email service and send the email
            boolean emailSent = emailService.sendEmail(emailRequest.getToEmail(), 
                emailRequest.getBody());
            if (emailSent) {
                return new ResponseEntity<>(HttpStatus.OK); // Email sent successfully
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Failed to send email
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error sending receipt: " + e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
