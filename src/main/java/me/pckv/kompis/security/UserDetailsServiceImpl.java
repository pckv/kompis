package me.pckv.kompis.security;

import me.pckv.kompis.service.UserService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        me.pckv.kompis.data.User user = userService.getUser(email);
        if (user == null) {
            throw new UsernameNotFoundException(email);
        }

        return new User(user.getEmail(), user.getPassword(), Collections.emptyList());
    }
}
