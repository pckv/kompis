package me.pckv.kompis.service;

import me.pckv.kompis.data.User;
import me.pckv.kompis.data.UserRepository;
import org.springframework.stereotype.Service;

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

    public User createUser(User newUser) {
        return repository.save(newUser);
    }

    public User getUser(Long id) {
        return repository.getOne(id);
    }

    public User replaceUser(User newUser, long id) {
        return repository.findById(id)
                .map(user -> {
                    user.setDisplayName(newUser.getDisplayName());
                    user.setEmail(newUser.getEmail());
                    user.setPassword(newUser.getPassword());
                    return repository.save(user);
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    return repository.save(newUser);
                });
    }

    public void deleteUserById(long id) {
        repository.deleteById(id);
    }
}
