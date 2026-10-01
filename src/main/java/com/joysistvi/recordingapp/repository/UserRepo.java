package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.model.User;

import java.util.List;

public interface UserRepo {

    List<User> getAllUsers();
    User readUserById(int id);
    List<User> searchUser(String keyword);
    boolean createUser(String username, String email);
    boolean updateUser(int id, String username, String email);
    boolean archiveUser(int id);
    boolean restoreUser(int id);
    boolean deleteUser(int id);
    List<User> getAllArchivedUsers();
}
