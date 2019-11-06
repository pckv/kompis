package me.pckv.kompis.controller;

import me.pckv.kompis.data.User;
import me.pckv.kompis.security.JwtManager;
import me.pckv.kompis.security.PasswordUtil;
import me.pckv.kompis.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private UserService userService;
    private JwtManager jwtManager;

    public UserController(UserService userService, JwtManager jwtManager) {
        this.userService = userService;
        this.jwtManager = jwtManager;
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

        // Verify the given password
        if (!PasswordUtil.verify(loginUser.getPassword(), user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        // Generate the JSON web token for the user and send it back
        String token = jwtManager.generateToken(user.getEmail());
        response.addHeader("Authorization", "Bearer " + token);

        return user;
    }
}
