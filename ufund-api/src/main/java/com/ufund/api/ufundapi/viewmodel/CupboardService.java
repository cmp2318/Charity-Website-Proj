package com.ufund.api.ufundapi.viewmodel;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.logging.Logger;

import com.ufund.api.ufundapi.model.Toy;
import com.ufund.api.ufundapi.persistence.CupboardFileDAO;
import com.ufund.api.ufundapi.model.FundBasket;

@Service
public class CupboardService {
    private static final Logger LOG = Logger.getLogger(CupboardService.class.getName());
    private CupboardFileDAO cupboardFileDAO;
    private BasketService basketService;

    private String logString = "Toy ID: ";

    public CupboardService(CupboardFileDAO cupboardFileDAO, BasketService basketService) {
        this.cupboardFileDAO = cupboardFileDAO;
        this.basketService = basketService;
    }

    public Toy getToyByID(int id) {
        return cupboardFileDAO.getToy(id);
    }

    public Toy[] getAllToys() throws IOException {
        return cupboardFileDAO.getToys();
    }

    public Toy createToy(Toy toy) throws IOException {
        return cupboardFileDAO.createToy(toy);
    }

    public Toy updateToy(Toy toy) throws IOException {
        updateToyAcrossAllBaskets(toy);
        return cupboardFileDAO.updateToy(toy);
    }

    public boolean deleteToy(int id) throws IOException {
        removeToyFromAllBaskets(id);
        return cupboardFileDAO.deleteToy(id);
    }

    public Toy[] findToys(String name) throws IOException {
        return cupboardFileDAO.findToys(name);
    }

    public void updateToyAcrossAllBaskets(Toy updatedToy) throws IOException {
        if (updatedToy == null) {
            LOG.warning("Updated toy is null. Exiting update process.");
            return;
        }
    
        LOG.info("Updating toy across all baskets for toy ID: " + updatedToy.getId());
        FundBasket[] allBaskets = basketService.getAllBaskets();
        
        // Check if the allBaskets is null before proceeding
        if (allBaskets == null) {
            LOG.warning("No baskets were fetched. Exiting update process.");
            return;
        }
    
        for (FundBasket basket : allBaskets) {
            // Check if the basket or its content is null
            if (basket == null || basket.getBasket() == null) {
                LOG.warning("Encountered null basket or basket content. Skipping.");
                continue;
            }
    
            // Iterate through the toys in the basket and update the matching toy

            for (Toy toy : basket.getBasket()) {
                if (toy.getId() == updatedToy.getId()) {

                    toy.setName(updatedToy.getName());

                    if(toy.getQuantity() >= updatedToy.getQuantity()){
                        toy.setQuantity(updatedToy.getQuantity());
                    }
                    toy.setCost(updatedToy.getCost());
                    toy.setType(updatedToy.getType());
                }
            }
        }
    }

    public void removeToyFromAllBaskets(int toyId) throws IOException {
        LOG.info("Removing toy from all baskets for toy ID: " + toyId);

        FundBasket[] allBaskets = basketService.getAllBaskets();
        if (allBaskets == null) {
            LOG.warning("No baskets were fetched. Exiting removal process.");
            return;
        }

        for (FundBasket basket : allBaskets) {
            if (basket == null || basket.getBasket() == null) {
                LOG.warning("Encountered null basket or basket content. Skipping.");
                continue;
            }

            boolean removed = basket.getBasket().removeIf(toy -> toy.getId() == toyId);
            if (removed) {
                LOG.info(logString + toyId + " removed from Basket ID: " + basket.getId());
            } else {
                LOG.info(logString + toyId + " not found in Basket ID: " + basket.getId());
            }
        }
    }
}