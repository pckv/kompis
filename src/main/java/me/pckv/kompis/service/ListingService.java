package me.pckv.kompis.service;

import me.pckv.kompis.data.Listing;
import me.pckv.kompis.data.User;
import me.pckv.kompis.repository.ListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ListingService {

    private ListingRepository repository;

    @Autowired
    public ListingService(ListingRepository repository) {
        this.repository = repository;
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
        Optional<Listing> listing = repository.findById(id);
        if (listing.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Listing with given id not found");
        }

        return listing.get();
    }

    /**
     * Delete listing if the provided user is the owner of the listing.
     *
     * @param listing the listing to delete
     * @param user    the user to compare with owner
     */
    public void deleteListing(Listing listing, User user) {
        if (!listing.isOwner(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User trying to delete listing must be owner of listing");
        }

        repository.delete(listing);
    }

    /**
     * Activate a listing if the provided user if the owner of the listing.
     *
     * @param listing the listing to activate
     * @param user the user to compare with owner
     */
    public void activateListing(Listing listing, User user) {
        if (!listing.isOwner(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User trying to activate listing must be owner of listing");
        }

        listing.setActive(true);
        repository.save(listing);
    }

    /**
     * Deactivate a listing if the provided user if the owner of the listing.
     *
     * @param listing the listing to deactivate
     * @param user the user to compare with owner
     */
    public void deactivateListing(Listing listing, User user) {
        if (!listing.isOwner(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User trying to deactivate listing must be owner of listing");
        }

        listing.setActive(false);
        repository.save(listing);
    }

    /**
     * Assign a user to the listing and update it.
     *
     * @param listing  the listing to assign the user to
     * @param assignee the user to assign to the listing
     */
    public void assignUserToListing(Listing listing, User assignee) {
        if (listing.hasAssignee()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Listing already has an assignee");
        }
        listing.setAssignee(assignee);
        repository.save(listing);
    }

    /**
     * If the current authorized user is the owner of the listing with the given ID,
     * the assignee will be removed from the listing. If the current authorized user
     * is assigned to the listing, they will remove themselves from the listing.
     *
     * @param listing the listing to unassign
     * @param user    the user that unassigns the listing (must be owner or assignee)
     */
    public void unassignUserFromListing(Listing listing, User user) {
        if (!listing.isOwner(user) && !listing.isAssignee(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User trying to unassign listing must be owner or assigned");
        }

        listing.setAssignee(null);
        repository.save(listing);
    }
}
