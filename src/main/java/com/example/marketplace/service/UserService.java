package com.example.marketplace.service;

import com.example.marketplace.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.example.marketplace.model.User;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findUserById(int id) {
        return userRepository.findById(id);
    }

    public Optional<User> authenticate(String username, String password) {
        return userRepository.findByUsername(username).filter(user -> user.getPassword().equals(password));
    }

    public Optional<User> createUser(String username, String email, String password, boolean admin) {

        if (userRepository.findByUsername(username).isPresent()) {
            return Optional.empty();
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setAdmin(admin);
        User savedUser = userRepository.save(user);

        return Optional.of(savedUser);
    }
}
