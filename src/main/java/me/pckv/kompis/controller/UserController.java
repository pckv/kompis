package me.pckv.kompis.controller;

import me.pckv.kompis.data.User;
import me.pckv.kompis.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    List<User> all() {
        return userService.getAllUsers();
    }

    @PostMapping("/users")
    User newUser(@RequestBody User newUser) {
        return userService.createUser(newUser);
    }

    @GetMapping("/users/{id}")
    User one(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PutMapping("/users/{id}")
    User replaceUser(@RequestBody User newUser, @PathVariable long id) {
        return userService.replaceUser(newUser, id);
    }

    @DeleteMapping("/user/{id}")
    void deleteUser(@PathVariable long id) {
        userService.deleteById(id);
    }
}
