package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.model.User;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    private final DbConnection dbConnection; // Composition

    // Constructor injection
    public UserRepositoryImpl(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT id, username, role FROM users";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query);
             ResultSet res = prep.executeQuery()) {

            while (res.next()) {
                users.add(new User(
                        res.getInt("id"),
                        res.getString("username"),
                        res.getString("role")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Read Users: " + e.getMessage());
        }

        return users;
    }

    @Override
    public boolean registerUser(String username, String password) {
        // Self-registration always creates a regular USER account.
        // Admin accounts are assigned directly in the database.
        String query = "INSERT INTO users (username, password, role) VALUES (?, ?, 'USER')"; // Anti-SQL Injection

        // Hash the password securely
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        try (Connection connection = dbConnection.connect();
             PreparedStatement prep = connection.prepareStatement(query)) {

            prep.setString(1, username);
            prep.setString(2, hashedPassword);

            int rows = prep.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Register User Error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public User login(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement prep = connection.prepareStatement(query)) {

            prep.setString(1, username);
            ResultSet rs = prep.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");

                if (BCrypt.checkpw(password, storedHash)) {
                    return new User(rs.getInt("id"), rs.getString("username"),
                            rs.getString("role"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Login Error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean deleteUser(int id) {
        String query = "DELETE FROM users WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, id);
            int rows = prep.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Delete User: " + e.getMessage());
        }
        return false;
    }
}