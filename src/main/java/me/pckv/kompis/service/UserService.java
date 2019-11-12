package me.pckv.kompis.service;

import me.pckv.kompis.data.Listing;
import me.pckv.kompis.data.User;
import me.pckv.kompis.repository.ListingRepository;
import me.pckv.kompis.repository.UserRepository;
import me.pckv.kompis.security.JwtManager;
import me.pckv.kompis.security.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository repository;
    private JwtManager jwtManager;
    private ListingRepository listingRepository;

    @Autowired
    public UserService(UserRepository repository, JwtManager jwtManager, ListingRepository listingRepository) {
        this.repository = repository;
        this.jwtManager = jwtManager;
        this.listingRepository = listingRepository;
    }

    /**
     * Create a user if no user with the same email exists.
     *
     * @param user the user to create
     * @return the user that is created
     */
    public User createUser(User user) {
        User existingUser = repository.findByEmail(user.getEmail());
        if (existingUser != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with given email already exists");
        }

        user.setPassword(PasswordUtil.encrypt(user.getPassword()));
        return repository.save(user);
    }

    /**
     * Find a user by id and return it.
     *
     * @param id the id of the user
     * @return user if found
     */
    public Optional<User> getUser(Long id) {
        Optional<User> user = repository.findById(id);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given id was not found");
        }

        return user;
    }

    /**
     * Find a user by email and return it.
     *
     * @param email the email of the user
     * @return user if found
     */
    public User getUser(String email) {
        User user = repository.findByEmail(email);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given email was not found");
        }

        return user;
    }

    /**
     * Returns true if a user with the given email exists.
     *
     * @param email the email of the user
     * @return true if the user exists
     */
    public boolean userExists(String email) {
        return repository.existsByEmail(email);
    }

    /**
     * Delete a user from the repository.
     *
     * @param user the user to delete
     */
    public void deleteUser(User user) {
        List<Listing> ownerListings = listingRepository.findByOwner_Id(user.getId());
        for (Listing listing : ownerListings) {
            listingRepository.delete(listing);
        }
        List<Listing> assigneeListings = listingRepository.findByAssignee_Id(user.getId());
        for (Listing listing : assigneeListings) {
            listing.setAssignee(null);
            listingRepository.save(listing);
        }
        repository.delete(user);
    }

    /**
     * Verify password given for the user and return a new JSON web token.
     *
     * @param user     the user to login to
     * @param password the password of the account to login to
     * @return a new JSON web token
     */
    public String login(User user, String password) {
        // Verify the given password
        if (!PasswordUtil.verify(password, user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Provided password was incorrect");
        }

        // Generate the JSON web token for the user and send it back
        return jwtManager.generateToken(user.getEmail());
    }
}
