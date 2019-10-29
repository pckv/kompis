package me.pckv.kompis.controller;

import me.pckv.kompis.data.Listing;
import me.pckv.kompis.service.ListingService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ListingController {

    private ListingService listingService;

    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }

    @PostMapping("/listing")
    public Listing newListing(@RequestBody Listing listing) {
        return listingService.createListing(newListing(listing));
    }

    @DeleteMapping("listing/{id}")
    public void deleteListingById(@PathVariable long id) {

    }
}
