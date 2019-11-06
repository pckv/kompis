package me.pckv.kompis.controller;

import me.pckv.kompis.annotation.Authorized;
import me.pckv.kompis.annotation.LoggedIn;
import me.pckv.kompis.data.Listing;
import me.pckv.kompis.data.User;
import me.pckv.kompis.service.ListingService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/listing")
public class ListingController {

    private ListingService listingService;

    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }

    /**
     * Create a new listing and set the logged in user as the owner.
     *
     * @param listing the listing to create
     * @param owner   the logged in user that will be the owner of the listing
     * @return the created listing
     */
    @Authorized
    @PostMapping
    public Listing createListing(@RequestBody Listing listing, @LoggedIn User owner) {
        return listingService.createListing(listing, owner);
    }

    /**
     * Assign a user to the listing and update it.
     *
     * @param id       the id of the listing
     * @param assignee the user to assign to the listing
     * @return the updated listing
     */
    @Authorized
    @GetMapping("/{id}/assign")
    public Listing assignListing(@PathVariable long id, @LoggedIn User assignee) {
        Listing listing = listingService.getListing(id);
        return listingService.assignUserToListing(listing, assignee);
    }

    /**
     * Activate a listing if the logged in user is the owner of the listing.
     *
     * @param id   the id of the
     * @param user the logged in user to compare with owner
     * @return the updated listing
     */
    @Authorized
    @GetMapping("/{id}/activate")
    public Listing activateListing(@PathVariable long id, @LoggedIn User user) {
        Listing listing = listingService.getListing(id);
        return listingService.activateListing(listing, user);
    }

    /**
     * Deactivate a listing if the logged in user is the owner of the listing.
     *
     * @param id   the id of the
     * @param user the logged in user to compare with owner
     * @return the updated listing
     */
    @Authorized
    @GetMapping("/{id}/deactivate")
    public Listing deactivateListing(@PathVariable long id, @LoggedIn User user) {
        Listing listing = listingService.getListing(id);
        return listingService.deactivateListing(listing, user);
    }

    /**
     * Delete listing if the logged in user is the owner of the listing.
     *
     * @param id   the id of the listing to delete
     * @param user the logged in user to compare with owner
     */
    @Authorized
    @DeleteMapping("/{id}")
    public void deleteListing(@PathVariable long id, @LoggedIn User user) {
        Listing listing = listingService.getListing(id);
        listingService.deleteListing(listing, user);
    }

    @Authorized
    @GetMapping("/{id}")
    public Listing getListing(@PathVariable long id) {
        return listingService.getListing(id);
    }

    /**
     * Get all listings.
     *
     * @return a list of all listings.
     */
    @Authorized
    @GetMapping
    public List<Listing> getAllListings() {
        return listingService.getAllListings();
    }
}
