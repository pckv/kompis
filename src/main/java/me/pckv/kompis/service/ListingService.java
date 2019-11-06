package me.pckv.kompis.service;

import me.pckv.kompis.data.Listing;
import me.pckv.kompis.data.User;
import me.pckv.kompis.repository.ListingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ListingService {
    private ListingRepository repository;

    public ListingService(ListingRepository repository) {
        this.repository = repository;
    }

    /**
     * Create a new listing and set the logged in user as the owner.
     *
     * @param listing the listing to create
     * @param owner   the user that will be the owner of the listing
     * @return the created listing
     */
    public Listing createListing(Listing listing, User owner) {
        listing.setOwner(owner);
        return repository.save(listing);
    }

    /**
     * Find a listing by id and return it.
     *
     * @param id the id of the listing to find
     * @return the found listing
     */
    public Listing getListing(Long id) {
        Listing listing = repository.getOne(id);
        if (listing == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return repository.getOne(id);
    }

    /**
     * Update listing by saving it to the repository again.
     *
     * @param listing the listing to update
     * @return the updated listing
     */
    public Listing updateListing(Listing listing) {
        return repository.save(listing);
    }

    /**
     * Get all listings.
     *
     * @return a list of all listings.
     */
    public List<Listing> getAllListings() {
        return repository.findAll();
    }

    /**
     * Activate a listing.
     *
     * @param listing the listing to activate
     */
    public void activateListing(Listing listing) {
        listing.setActive(true);
    }

    /**
     * Deactivate a listing.
     *
     * @param listing the listing to deactivate
     */
    public void deactivateListing(Listing listing) {
        listing.setActive(false);
    }

    /**
     * Assign a user to the listing and update it.
     *
     * @param listing  the listing to assign the user to
     * @param assignee the user to assign to the listing
     * @return the updated listing
     */
    public Listing assignUserToListing(Listing listing, User assignee) {
        listing.setAssignee(assignee);
        return updateListing(listing);
    }
}
