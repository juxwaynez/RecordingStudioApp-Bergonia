package com.joysistvi.recordingapp.controller;

import com.joysistvi.recordingapp.model.User;
import com.joysistvi.recordingapp.service.UserService;

import java.util.List;

public class UserController {

    private final UserService userService; // Composition

    // Constructor injection
    public UserController(UserService userService) {
        this.userService = userService;
    }

    public List<User> handleViewAllUsers() {
        return userService.getAllUsers();
    }

    public boolean handleRegister(String username, String password) {
        return userService.registerUser(username, password);
    }

    public User handleLogin(String username, String password) {
        return userService.login(username, password);
    }

    public boolean handleDeleteUser(int id) {
        return userService.deleteUser(id);
    }
}