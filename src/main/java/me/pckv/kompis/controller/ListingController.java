package me.pckv.kompis.controller;

import me.pckv.kompis.annotation.Authorized;
import me.pckv.kompis.annotation.LoggedIn;
import me.pckv.kompis.data.Listing;
import me.pckv.kompis.data.User;
import me.pckv.kompis.service.ListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/listings")
public class ListingController {

    private ListingService listingService;

    @Autowired
    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }

    /**
     * Gets a list of all listings.
     *
     * @return a list of all listings
     */
    @Authorized
    @GetMapping
    public List<Listing> getAllListings() {
        return listingService.getAllListings();
    }

    /**
     * Creates a new listing owned by the current logged in user.
     *
     * @param listing the listing to create
     * @param owner the logged in user that will be the owner of the listing
     * @return the created listing
     */
    @Authorized
    @PostMapping
    public Listing createListing(@RequestBody Listing listing, @LoggedIn User owner) {
        return listingService.createListing(listing, owner);
    }

    /**
     * Gets the listing with the given ID.
     *
     * @param listing the listing assigned by the path variable
     * @return the listing with the given ID
     */
    @Authorized
    @GetMapping("/{listingId}")
    public Listing getListing(Listing listing) {
        return listing;
    }

    /**
     * Delete the listing with the given ID if it is owned by the current authorized user.
     *
     * @param listing the listing assigned by the path variable
     * @param user the logged in user to compare with owner
     */
    @Authorized
    @DeleteMapping("/{listingId}")
    public void deleteListing(Listing listing, @LoggedIn User user) {
        listingService.deleteListing(listing, user);
    }

    /**
     * Activate the listing with the given ID if it is owned by the current authorized user.
     *
     * @param listing the listing assigned by the path variable
     * @param user the logged in user to compare with owner
     */
    @Authorized
    @GetMapping("/{listingId}/activate")
    public void activateListing(Listing listing, @LoggedIn User user) {
        listingService.activateListing(listing, user);
    }

    /**
     * Deactivate the listing with the given ID if it is owned by the current authorized user.
     *
     * @param listing the listing assigned by the path variable
     * @param user the logged in user to compare with owner
     */
    @Authorized
    @GetMapping("/{listingId}/deactivate")
    public void deactivateListing(Listing listing, @LoggedIn User user) {
        listingService.deactivateListing(listing, user);
    }

    /**
     * Assign the current logged in user to the listing with the given ID.
     *
     * @param listing the listing assigned by the path variable
     * @param assignee the user to assign to the listing
     */
    @Authorized
    @GetMapping("/{listingId}/assign")
    public void assignListing(Listing listing, @LoggedIn User assignee) {
        listingService.assignUserToListing(listing, assignee);
    }

    /**
     * If the current authorized user is the owner of the listing with the given ID,
     * the assignee will be removed from the listing. If the current authorized user
     * is assigned to the listing, they will remove themselves from the listing.
     *
     * @param listing the listing to unassign
     * @param user    the user that unassigns the listing (must be owner or assignee)
     */
    @Authorized
    @GetMapping("/{listingId}/unassign")
    public void unassignListing(Listing listing, @LoggedIn User user) {
        listingService.unassignUserFromListing(listing, user);
    }
}
