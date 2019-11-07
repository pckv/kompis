package me.pckv.kompis.service;

import me.pckv.kompis.data.Listing;
import me.pckv.kompis.data.User;
import me.pckv.kompis.repository.ListingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

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
        Optional<Listing> listing = repository.findById(id);
        if (listing.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return listing.get();
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
     * Activate a listing if the provided user if the owner of the listing.
     *
     * @param listing the listing to activate
     * @param user the user to compare with owner
     * @return the updated listing
     */
    public Listing activateListing(Listing listing, User user) {
        if (!listing.getOwner().getEmail().equals(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        listing.setActive(true);
        return repository.save(listing);
    }

    /**
     * Deactivate a listing if the provided user if the owner of the listing.
     *
     * @param listing the listing to deactivate
     * @param user the user to compare with owner
     * @return the updated listing
     */
    public Listing deactivateListing(Listing listing, User user) {
        if (!listing.getOwner().getEmail().equals(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        listing.setActive(false);
        return repository.save(listing);
    }

    /**
     * Delete listing if the provided user is the owner of the listing.
     *
     * @param listing the listing to delete
     * @param user    the user to compare with owner
     */
    public void deleteListing(Listing listing, User user) {
        if (!listing.getOwner().getEmail().equals(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        repository.delete(listing);
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
        return repository.save(listing);
    }
}
