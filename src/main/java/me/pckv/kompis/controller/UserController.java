package me.pckv.kompis.controller;

import me.pckv.kompis.annotation.Authorized;
import me.pckv.kompis.annotation.LoggedIn;
import me.pckv.kompis.data.User;
import me.pckv.kompis.service.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Create a user if no user with the same email exists.
     *
     * @param newUser the user to create
     * @return the user that is created
     */
    @PostMapping
    public User newUser(@RequestBody User newUser) {
        return userService.createUser(newUser);
    }

    /**
     * Find a user by id and return it as a JSON object.
     *
     * @param id the id of the user to find and return
     * @return the user as JSON object
     */
    @Authorized
    @GetMapping("/{id}")
    public User one(@PathVariable Long id) {
        return userService.getUser(id);
    }

    /**
     * Delete the logged in user.
     *
     * @param user the logged in user that will be deleted
     */
    @Authorized
    @DeleteMapping
    public void deleteUser(@LoggedIn User user) {
        userService.deleteUser(user);
    }

    /**
     * Verify that the user exists and the password is correct,
     * then return a JSON web token in the header and the user as a JSON object.
     *
     * @param loginUser the user to login to
     * @return the user as a JSON object and the JSON web token in the Authorization header
     */
    @PostMapping("/login")
    public User login(@RequestBody User loginUser, HttpServletResponse response) {
        User user = userService.getUser(loginUser.getEmail());

        String token = userService.login(user, loginUser.getPassword());
        response.addHeader("Authorization", "Bearer " + token);

        return user;
    }

    /**
     * Return the current user as a JSON object.
     *
     * @param current currently logged in user
     * @return the current user as a JSON object
     */
    @Authorized
    @GetMapping("/current")
    public User currentUser(@LoggedIn User current) {
        return current;
    }
}
