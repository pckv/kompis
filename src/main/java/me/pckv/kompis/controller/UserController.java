package me.pckv.kompis.controller;

import me.pckv.kompis.data.User;
import me.pckv.kompis.service.UserService;
import org.apache.coyote.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private UserService userService;

    @RequestMapping("/users")
    public User getUser() {
        return userService.getUser;
    }

    @PostMapping("/create")
    public Response createUser() {

    }
}
