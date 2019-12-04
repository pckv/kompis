package me.pckv.kompis.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import me.pckv.kompis.annotation.Authorized;
import me.pckv.kompis.annotation.LoggedIn;
import me.pckv.kompis.data.User;
import me.pckv.kompis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Creates a new user that can be logged into.
     *
     * @param newUser the user to create
     * @return the user that is created
     */
    @PostMapping
    public User createUser(@Valid @RequestBody User newUser) {
        return userService.createUser(newUser);
    }

    /**
     * Receive authorization for use with endpoints requiring authorization.
     *
     * @param loginUser the user to login to
     * @return the user as a JSON object and the JSON web token in the Authorization header
     */
    @PostMapping("/authorize")
    public User login(@RequestBody User loginUser, HttpServletResponse response) {
        User user = userService.getUser(loginUser.getEmail());

        // Update the firebase token on login if provided
        if (loginUser.hasFirebaseToken()) {
            userService.setFirebaseToken(user, loginUser.getFirebaseToken());
        }

        String token = userService.login(user, loginUser.getPassword());
        response.addHeader("Authorization", "Bearer " + token);

        return user;
    }

    /**
     * Get the user with the given ID.
     *
     * @param id the id of the user to find and return
     * @return the user as JSON object
     */
    @Authorized
    @GetMapping("/{id}")
    public User getUser(@PathVariable long id) {
        return userService.getUser(id);
    }

    /**
     * Get the current authorized user.
     *
     * @param current currently logged in user
     * @return the current user as a JSON object
     */
    @Authorized
    @GetMapping("/current")
    public User currentUser(@LoggedIn User current) {
        return current;
    }

    /**
     * Delete the current authorized user. All listings where this user is
     * the assignee will be unassigned and all listings where this user is
     * the owner will be deleted.
     *
     * The client should get rid of the Authorization token manually.
     *
     * @param user the logged in user that will be deleted
     */
    @Authorized
    @DeleteMapping("/current")
    public void deleteUser(@LoggedIn User user) {
        userService.deleteUser(user);
    }

    /**
     * Update the firebase token for the current authorized user.
     *
     * @param token the new firebase token
     * @param current the currently logged in user
     */
    @Authorized
    @PutMapping("/current/firebase")
    public void putFirebaseToken(@RequestParam String token, @LoggedIn User current) {
        userService.setFirebaseToken(current, token);
    }
}
