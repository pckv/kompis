package me.pckv.kompis.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.util.List;
import java.util.Optional;
import me.pckv.kompis.data.Assignee;
import me.pckv.kompis.data.Listing;
import me.pckv.kompis.data.User;
import me.pckv.kompis.repository.ListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
     * @param owner the user that will be the owner of the listing
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Listing with given id not found");
        }

        return listing.get();
    }

    /**
     * Delete listing if the provided user is the owner of the listing.
     *
     * @param listing the listing to delete
     * @param owner the user to compare with owner
     */
    public void deleteListing(Listing listing, User owner) {
        if (!listing.isOwner(owner)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "User trying to delete listing must be owner of listing");
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
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "User trying to activate listing must be owner of listing");
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
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "User trying to deactivate listing must be owner of listing");
        }

        listing.setActive(false);
        repository.save(listing);
    }

    /**
     * Assign a assignee to the listing and update it.
     *
     * @param listing the listing to assign the user to
     * @param assignee the assignee to assign to the listing
     */
    public void assignAssigneeToListing(Listing listing, Assignee assignee) {
        if (listing.hasAssignee()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Listing already has an assignee");
        }

        // Send notification to owner about the new assignee
        if (listing.getOwner().hasFirebaseToken()) {
            String assigneeName = assignee.getUser().getDisplayName();
            Message message = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(assigneeName)
                            .setBody("Assigned themselves to your listing " + listing.getTitle())
                            .build())
                    .putData("asString", assigneeName + " assigned themselves to your listing "
                            + listing.getTitle())
                    .setToken(listing.getOwner().getFirebaseToken())
                    .build();

            FirebaseMessaging.getInstance().sendAsync(message);
        }

        listing.setAssignee(assignee);
        repository.save(listing);
    }

    /**
     * If the current authorized user is the owner of the listing with the given ID, the assignee
     * will be removed from the listing. If the current authorized user is assigned to the listing,
     * they will remove themselves from the listing.
     *
     * @param listing the listing to unassign
     * @param user the user that unassigns the listing (must be owner or assignee)
     */
    public void unassignAssigneeFromListing(Listing listing, User user) {
        if (!listing.isOwner(user) && !listing.isAssignee(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "User trying to unassign listing must be owner or assigned");
        }

        listing.setAssignee(null);
        repository.save(listing);
    }

    /**
     * Remove listings owned by the given user.
     *
     * @param user the user
     */
    public void removeListingsOwnedBy(User user) {
        List<Listing> ownerListings = repository.findByOwner(user);
        for (Listing listing : ownerListings) {
            repository.delete(listing);
        }
    }

    /**
     * Unassign listings where the given user is assigned.
     *
     * @param user the user
     */
    public void unassignListingsAssignedTo(User user) {
        List<Listing> assigneeListings = repository.findByAssigneeIsNotNull();
        for (Listing listing : assigneeListings) {
            if (listing.getAssignee().getUser().equals(user)) {
                listing.setAssignee(null);
                repository.save(listing);
            }
        }
    }
}
