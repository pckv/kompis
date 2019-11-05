package me.pckv.kompis.service;

import me.pckv.kompis.data.User;
import me.pckv.kompis.data.UserRepository;
import me.pckv.kompis.security.PasswordHasher;
import org.springframework.stereotype.Service;

import java.security.Principal;
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
        user.setPassword(PasswordHasher.hash(user.getPassword()));
        return repository.save(user);
    }

    public User getUser(Long id) {
        return repository.getOne(id);
    }

    public User getUser(String email) {
        return repository.findByEmail(email);
    }

    public User getUser(Principal principal) {
        return repository.findByEmail(principal.getName());
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
