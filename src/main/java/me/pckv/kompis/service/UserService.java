package me.pckv.kompis.service;

import java.util.Optional;
import me.pckv.kompis.data.User;
import me.pckv.kompis.repository.UserRepository;
import me.pckv.kompis.security.JwtManager;
import me.pckv.kompis.security.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private UserRepository repository;
    private JwtManager jwtManager;
    private ListingService listingService;

    @Autowired
    public UserService(UserRepository repository, JwtManager jwtManager,
            ListingService listingService) {
        this.repository = repository;
        this.jwtManager = jwtManager;
        this.listingService = listingService;
    }

    /**
     * Create a user if no user with the same email exists.
     *
     * @param user the user to create
     * @return the user that is created
     */
    public User createUser(User user) {
        if (repository.existsByEmail(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "User with given email already exists");
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
    public User getUser(Long id) {
        Optional<User> user = repository.findById(id);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "User with given id was not found");
        }

        return user.get();
    }

    /**
     * Find a user by email and return it.
     *
     * @param email the email of the user
     * @return user if found
     */
    public User getUser(String email) {
        Optional<User> user = repository.findByEmail(email);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "User with given email was not found");
        }

        return user.get();
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
        // Remove listings owned by the to-be-deleted user
        listingService.removeListingsOwnedBy(user);

        // Unassign listings where the to-be-deleted user is assigned
        listingService.unassignListingsAssignedTo(user);

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

    /**
     * Sets the firebase token for the given user.
     *
     * @param user the user to set the firebase token to
     * @param firebaseToken the firebase token
     */
    public void setFirebaseToken(User user, String firebaseToken) {
        user.setFirebaseToken(firebaseToken);
        repository.save(user);
    }
}
