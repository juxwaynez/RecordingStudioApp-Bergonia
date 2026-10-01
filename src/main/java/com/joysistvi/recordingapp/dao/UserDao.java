package com.joysistvi.recordingapp.dao;

import com.joysistvi.recordingapp.config.DbConnection;

import java.sql.*;

public class UserDao {

    // COMPOSITION
    private final DbConnection dbConnection;

    // CONSTRUCTOR INJECTION
    public UserDao(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    // READ ALL USERS
    public void readAllUsers() {
        String query = "SELECT * FROM users WHERE is_archived = 0";

        // TRY-WITH-RESOURCES FOR AUTO-CLOSING DATABASE CONNECTIONS
        try (Connection conn = dbConnection.connect();
             Statement stmnt = conn.createStatement();
             ResultSet result = stmnt.executeQuery(query)) {

            // TOP BORDER
            System.out.println("+-------+----------------------+------------------------------------+");
            // HEADER
            System.out.printf("| %-5s | %-20s | %-34s |%n", "ID", "Username", "Email");
            // HEADER DIVIDER
            System.out.println("+-------+----------------------+------------------------------------+");

            // EXTRACT AND DISPLAY ROWS
            while (result.next()) {
                int id = result.getInt("id");
                String username = result.getString("username");
                String email = result.getString("email");

                System.out.printf("| %-5d | %-20s | %-34s |%n", id, username, email);
            }

            // BOTTOM BORDER
            System.out.println("+-------+----------------------+------------------------------------+");

        } catch (SQLException e) {
            System.err.println("Get All Users: " + e.getMessage());
        }
    }

    // CREATE USER
    public boolean createUser(String username, String email) {

        // VALIDATION
        if (username == null || username.trim().isEmpty()) {
            System.out.println("Username is required.");
            return false;
        }

        if (email == null || email.trim().isEmpty()) {
            System.out.println("Email is required.");
            return false;
        }

        // PARAMETERIZED QUERY
        String query = "INSERT INTO users (username, email) VALUES (?, ?)";
        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            // SET WILDCARD VALUES
            prep.setString(1, username.trim());
            prep.setString(2, email.trim());

            int rows = prep.executeUpdate();

            if (rows > 0) {
                System.out.println("User " + username + " added successfully.\n");
                readAllUsers();
                return true;
            } else {
                System.out.println("Failed to add user.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Create User: " + e.getMessage());
            return false;
        }
    }

    // UPDATE USER
    public boolean updateUser(int id, String username, String email) {
        if (id <= 0) {
            System.out.println("Invalid user ID.");
            return false;
        }

        if (username == null || username.trim().isEmpty()) {
            System.out.println("Username is required.");
            return false;
        }

        if (email == null || email.trim().isEmpty()) {
            System.out.println("Email is required.");
            return false;
        }

        String query = "UPDATE users SET username = ?, email = ? WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setString(1, username.trim());
            prep.setString(2, email.trim());
            prep.setInt(3, id);

            int rows = prep.executeUpdate();

            if (rows > 0) {
                System.out.println("User ID " + id + " updated successfully.\n");
                readAllUsers();
                return true;
            } else {
                System.out.println("Failed to update user.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Update User: " + e.getMessage());
            return false;
        }
    }

    // ARCHIVE USER
    public boolean archiveUser(int id) {
        if (id <= 0) {
            System.out.println("Invalid user ID.");
            return false;
        }

        String query = "UPDATE users SET is_archived = 1 WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            // SET WILDCARD VALUES
            prep.setInt(1, id);

            int rows = prep.executeUpdate();

            if (rows > 0) {
                System.out.println("User " + id + " archived successfully.\n");
                readAllUsers();
                return true;
            } else {
                System.out.println("Failed to archive user.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Archive User: " + e.getMessage());
            return false;
        }
    }

    // RESTORE USER
    public boolean restoreUser(int id) {
        if (id <= 0) {
            System.out.println("Invalid user ID.");
            return false;
        }

        String query = "UPDATE users SET is_archived = 0 WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            // SET WILDCARD VALUES
            prep.setInt(1, id);

            int rows = prep.executeUpdate();

            if (rows > 0) {
                System.out.println("User " + id + " restored successfully.\n");
                readAllUsers();
                return true;
            } else {
                System.out.println("Failed to restore user.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Restore User: " + e.getMessage());
            return false;
        }
    }

    // DELETE USER
    public boolean deleteUser(int id) {
        if (id <= 0) {
            System.out.println("Invalid user ID.");
            return false;
        }

        String query = "DELETE FROM users WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            // SET WILDCARD VALUES
            prep.setInt(1, id);

            int rows = prep.executeUpdate();

            if (rows > 0) {
                System.out.println("User " + id + " deleted successfully.\n");
                readAllUsers();
                return true;
            } else {
                System.out.println("Failed to delete user.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Delete User: " + e.getMessage());
            return false;
        }
    }

    // READ USER BY ID
    public void readUserById(int id) {
        String query = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, id);
            ResultSet res = prep.executeQuery();

            System.out.println("+-------+----------------------+------------------------------------+");
            System.out.printf("| %-5s | %-20s | %-34s |%n", "ID", "Username", "Email");
            System.out.println("+-------+----------------------+------------------------------------+");

            if (res.next()) {
                System.out.printf("| %-5d | %-20s | %-34s |%n",
                        res.getInt("id"),
                        res.getString("username"),
                        res.getString("email"));
            }

            System.out.println("+-------+----------------------+------------------------------------+");

        } catch (SQLException e) {
            System.out.println("Read User By Id: " + e.getMessage());
        }
    }

    // SEARCH USER
    public void searchUser(String keyword) {

        // VALIDATION
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("Search keyword cannot be empty.");
            return;
        }

        // DATABASE ACCESS LOGIC
        String query = "SELECT * FROM users WHERE username LIKE ? OR email LIKE ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            String searchPattern = "%" + keyword.trim() + "%";
            prep.setString(1, searchPattern);
            prep.setString(2, searchPattern);

            ResultSet res = prep.executeQuery();

            // UI / VIEW
            System.out.println("+-------+----------------------+------------------------------------+");
            System.out.printf("| %-5s | %-20s | %-34s |%n", "ID", "Username", "Email");
            System.out.println("+-------+----------------------+------------------------------------+");

            while (res.next()) {
                System.out.printf("| %-5d | %-20s | %-34s |%n",
                        res.getInt("id"),
                        res.getString("username"),
                        res.getString("email"));
            }

            System.out.println("+-------+----------------------+------------------------------------+");

        } catch (SQLException e) {
            System.out.println("Search User: " + e.getMessage());
        }
    }

    // READ ALL ARCHIVED USERS
    public void readAllArchivedUsers() {
        String query = "SELECT * FROM users WHERE is_archived = 1";

        // TRY-WITH-RESOURCES FOR AUTO-CLOSING DATABASE CONNECTIONS
        try (Connection conn = dbConnection.connect();
             Statement stmnt = conn.createStatement();
             ResultSet result = stmnt.executeQuery(query)) {

            // TOP BORDER
            System.out.println("+-------+----------------------+------------------------------------+");
            // HEADER
            System.out.printf("| %-5s | %-20s | %-34s |%n", "ID", "Username", "Email");
            // HEADER DIVIDER
            System.out.println("+-------+----------------------+------------------------------------+");

            // EXTRACT AND DISPLAY ROWS
            while (result.next()) {
                int id = result.getInt("id");
                String username = result.getString("username");
                String email = result.getString("email");

                System.out.printf("| %-5d | %-20s | %-34s |%n", id, username, email);
            }

            // BOTTOM BORDER
            System.out.println("+-------+----------------------+------------------------------------+");

        } catch (SQLException e) {
            System.err.println("Get All Archived Users: " + e.getMessage());
        }
    }
}