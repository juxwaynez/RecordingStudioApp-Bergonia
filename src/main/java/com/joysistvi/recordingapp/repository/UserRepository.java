package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.model.User;

import java.util.List;

public interface UserRepository {

    List<User> getAllUsers();

    boolean registerUser(String username, String password);

    User login(String username, String password);

    boolean deleteUser(int id);
}