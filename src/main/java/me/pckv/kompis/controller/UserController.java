package me.pckv.kompis.controller;

import me.pckv.kompis.annotation.Authorized;
import me.pckv.kompis.annotation.LoggedIn;
import me.pckv.kompis.data.User;
import me.pckv.kompis.service.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> all() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User newUser(@RequestBody User newUser) {
        return userService.createUser(newUser);
    }

    @GetMapping("/{id}")
    public User one(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @Authorized
    @PutMapping("/{id}")
    public User replaceUser(@PathVariable long id, @RequestBody User newUser) {
        return userService.replaceUser(id, newUser);
    }

    @Authorized
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        userService.deleteById(id);
    }

    @PostMapping("/login")
    public User login(@RequestBody User loginUser, HttpServletResponse response) {
        User user = userService.getUser(loginUser.getEmail());

        String token = userService.login(user, loginUser.getPassword());
        response.addHeader("Authorization", "Bearer " + token);

        return user;
    }

    @Authorized
    @GetMapping("/current")
    public User currentUser(@LoggedIn User current) {
        return current;
    }
}
