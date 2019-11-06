package me.pckv.kompis.service;

import me.pckv.kompis.data.User;
import me.pckv.kompis.data.UserRepository;
import me.pckv.kompis.security.PasswordUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    private UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User createUser(User user) {
        user.setPassword(PasswordUtil.encrypt(user.getPassword()));
        return repository.save(user);
    }

    public User getUser(Long id) {
        return repository.getOne(id);
    }

    public User getUser(String email) {
        User user = repository.findByEmail(email);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return user;
    }

    public User replaceUser(long userId, User newUser) {
        return repository.findById(userId)
                .map(user -> {
                    user.setDisplayName(newUser.getDisplayName());
                    user.setEmail(newUser.getEmail());
                    user.setPassword(newUser.getPassword());
                    return repository.save(user);
                })
                .orElseGet(() -> {
                    newUser.setId(userId);
                    return repository.save(newUser);
                });
    }

    public void deleteById(long id) {
        repository.deleteById(id);
    }
}
