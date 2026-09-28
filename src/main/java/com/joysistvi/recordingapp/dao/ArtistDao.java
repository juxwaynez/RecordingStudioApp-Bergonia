package com.joysistvi.recordingapp.dao;

import com.joysistvi.recordingapp.config.DbConnection;

import java.sql.*;

public class ArtistDao {

    // COMPOSITION
        private final DbConnection dbConnection;

    // CONSTRUCTOR INJECTION
        public ArtistDao(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    // CRUD OPERATION
        public void readAllArtists() {
        String query = "SELECT * FROM artists WHERE is_archived = 0";

        // TRY-WITH-RESOURCES FOR AUTO-CLOSING DATABASE CONNECTIONS
        try (Connection conn = dbConnection.connect();
             Statement stmnt = conn.createStatement();
             ResultSet result = stmnt.executeQuery(query)) {

            // TOP BORDER
            System.out.println("+-------+----------------------+");
            // HEADER
            System.out.printf("| %-5s | %-20s |%n", "ID", "Name");
            // HEADER DIVIDER
            System.out.println("+-------+----------------------+");

            // EXTRACT AND DISPLAY ROWS
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");

                System.out.printf("| %-5d | %-20s |%n", id, name);
            }

            // BOTTOM BORDER
            System.out.println("+-------+----------------------+");

        } catch (SQLException e) {
            System.err.println("Get All Artists: " + e.getMessage());
        }
    }

    // CREATE ARTIST
    public boolean createArtist(String name) {

        // VALIDATION
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Artist name is required");
            return false;
        }

        // PARAMETERIZED QUERY
        String query = "INSERT INTO artists (name) VALUES (?)";
        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            // SET WILDCARD VALUES
            prep.setString(1, name.trim());

            int rows = prep.executeUpdate();

            if (rows > 0) {
                System.out.println("Artist " + name + " added successfully.\n");
                readAllArtists();
                return true;
            } else {
                System.out.println("Failed to add artist.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Create Artist: " + e.getMessage());
            return false;
        }
    }

    // UPDATE ARTIST
    public boolean updateArtist(String name, int id) {
        if (id <= 0) {
            System.out.println("Invalid artist ID.");
            return false;
        }

        if (name == null || name.trim().isEmpty()) {
            System.out.println("Artist name is required");
            return false;
        }

        String query = "UPDATE artists SET name = ? WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setString(1, name.trim());
            prep.setInt(2, id);

            int rows = prep.executeUpdate();

            if (rows > 0) {
                System.out.println("Artist " + name + " updated successfully\n");
                readAllArtists();
                return true;
            } else {
                System.out.println("Failed to update artist.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Update Artist: " + e.getMessage());
            return false;
        }
    }

    // ARCHIVE ARTIST
    public boolean archiveArtist(int id) {
        if (id <= 0) {
            System.out.println("Invalid artist ID.");
            return false;
        }

        String query = "UPDATE artists SET is_archived = 1 WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            // set wildcard values
            prep.setInt(1, id);

            int rows = prep.executeUpdate();

            if (rows > 0) {
                System.out.println("Artist " + id + " archived successfully.\n");
                readAllArtists();
                return true;
            } else {
                System.out.println("Failed to archive artist.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Archive Artist: " + e.getMessage());
            return false;
        }
    }

    // RESTORE ARTIST
    public boolean restoreArtist(int id) {
        if (id <= 0) {
            System.out.println("Invalid artist ID.");
            return false;
        }

        String query = "UPDATE artists SET is_archived = 0 WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            // SET WILD CARD VALUES
            prep.setInt(1, id);

            int rows = prep.executeUpdate();

            if (rows > 0) {
                System.out.println("Artist " + id + " restored successfully.\n");
                readAllArtists();
                return true;
            } else {
                System.out.println("Failed to restore artist.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Restore Artist: " + e.getMessage());
            return false;
        }
    }

    // DELETE ARTIST
    public boolean deleteArtist(int id) {
        if (id <= 0) {
            System.out.println("Invalid artist ID.");
            return false;
        }

        String query = "DELETE FROM artists WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            // SET WILDCARD VALUES
            prep.setInt(1, id);

            int rows = prep.executeUpdate();

            if (rows > 0) {
                System.out.println("Artist " + id + " deleted successfully.\n");
                readAllArtists();
                return true;
            } else {
                System.out.println("Failed to delete artist.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Delete Artist: " + e.getMessage());
            return false;
        }
    }

    // RESTORE ARTIST BY ID
    public void readArtistById(int id) {
        String query = "SELECT * FROM artists WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, id);
            ResultSet res = prep.executeQuery();

            System.out.println("+-------+----------------------+");
            System.out.printf("| %-5s | %-20s |%n", "ID", "Name");
            System.out.println("+-------+----------------------+");

            if (res.next()) {
                System.out.printf("| %-5d | %-20s |%n", res.getInt("id"), res.getString("name"));
            }

            System.out.println("+-------+----------------------+");

        } catch (SQLException e) {
            System.out.println("Read Artist By Id: " + e.getMessage());
        }
    }

    // SOC PRINCIPLE
    public void searchArtist(String keyword) {

        // VALIDATION
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("Search keyword cannot be empty.");
            return;
        }

        // DATABASE ACCESS LOGIC
        String query = "SELECT * FROM artists WHERE name LIKE ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setString(1, "%" + keyword.trim() + "%");
            ResultSet res = prep.executeQuery();

            // UI / VIEW
            System.out.println("+-------+----------------------+");
            System.out.printf("| %-5s | %-20s |%n", "ID", "Name");
            System.out.println("+-------+----------------------+");

            while (res.next()) {
                System.out.printf("| %-5d | %-20s |%n", res.getInt("id"), res.getString("name"));
            }

            System.out.println("+-------+----------------------+");

        } catch (SQLException e) {
            System.out.println("Search Artist: " + e.getMessage());
        }
    }

    // READ ARCHIVES ARTISTS
    public void readAllArchivedArtists() {
        String query = "SELECT * FROM artists WHERE is_archived = 1";

        // TRY-WITH-RESOURCES FOR AUTO-CLOSING DATABASE CONNECTIONS
        try (Connection conn = dbConnection.connect();
             Statement stmnt = conn.createStatement();
             ResultSet result = stmnt.executeQuery(query)) {

            // TOP BORDER
            System.out.println("+-------+----------------------+");
            // HEADER
            System.out.printf("| %-5s | %-20s |%n", "ID", "Name");
            // HEADER DIVIDER
            System.out.println("+-------+----------------------+");

            // EXTRACT AND DISPLAY ROWS
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");

                System.out.printf("| %-5d | %-20s |%n", id, name);
            }

            // BOTTOM BORDER
            System.out.println("+-------+----------------------+");

        } catch (SQLException e) {
            System.err.println("Get All Artists: " + e.getMessage());
        }
    }
}






/*
    ======== IMPORTANT NOTES =========
    Hard Delete / Soft Delete (Archive)
    SQL Categories
    DDL
    DML -> Data Manipulation Language
        (Insert, Update, Delete) : prepareStatement() -> executeUpdate()
    DQL -> Data Query Language
        (Select) : createStatement() -> executeQuery()

    inheritance: is-a relationship (tightly coupled)
    composition: has-a relationship (loosely coupled)

 */

/**
 * DATA ACCESS OBJECT (DAO) PATTERN EXPLANATION
 * --------------------------------------------
 *
 * WHAT IS A DAO?
 * DAO stands for Data Access Object. It is a fundamental design pattern in
 * Java used to separate low-level data accessing operations (SQL queries)
 * from high-level business logic.
 *
 * PURPOSE OF A DAO CLASS:
 * 1. Abstraction & Decoupling:
 *    It acts as a "middleman" between your Java Application and the Database.
 *    The rest of your code calls simple Java methods (e.g., songDao.addSong(song))
 *    without needing to know the underlying SQL queries or database details.
 *
 * 2. Centralized Database Operations (CRUD):
 *    All Create, Read, Update, and Delete operations for a specific entity
 *    (like a 'Song' or 'User') are organized in one dedicated class instead
 *    of being scattered throughout the project.
 *
 * 3. Maintainability & Flexibility:
 *    If the database structure, SQL queries, or database type (e.g., switching
 *    from MySQL to PostgreSQL) change in the future, you only need to modify
 *    the DAO class rather than touching the entire application codebase.
 */





