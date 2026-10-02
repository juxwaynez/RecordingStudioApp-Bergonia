package com.joysistvi.recordingapp.service;

import com.joysistvi.recordingapp.model.User;
import com.joysistvi.recordingapp.repository.UserRepository;

import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository; // Composition

    // Constructor injection
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public boolean registerUser(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            System.out.println("Username is required.");
            return false;
        }
        if (password == null || password.length() < 6) {
            System.out.println("Password must be at least 6 characters.");
            return false;
        }
        return userRepository.registerUser(username.trim(), password);
    }

    @Override
    public User login(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null
                || password.isEmpty()) {
            System.out.println("Username and password are required.");
            return null;
        }
        return userRepository.login(username.trim(), password);
    }

    @Override
    public boolean deleteUser(int id) {
        if (id <= 0) {
            System.out.println("Invalid user ID.");
            return false;
        }
        return userRepository.deleteUser(id);
    }
}