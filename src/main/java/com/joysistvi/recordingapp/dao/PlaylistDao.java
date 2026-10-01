package com.joysistvi.recordingapp.dao;

import com.joysistvi.recordingapp.config.DbConnection;

import java.sql.*;

public class PlaylistDao {

    // COMPOSITION
    private final DbConnection dbConnection;

    // CONSTRUCTOR INJECTION
    public PlaylistDao (DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    // READ ALL PLAYLISTS
    public void readAllPlaylists() {
        String query = "SELECT * FROM playlists WHERE is_archived = 0";

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
            System.err.println("Get All Playlists: " + e.getMessage());
        }
    }

    // CREATE PLAYLIST
    public boolean createPlaylist(String name) {

        // VALIDATION
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Playlist name is required.");
            return false;
        }

        // PARAMETERIZED QUERY
        String query = "INSERT INTO playlists (name) VALUES (?)";
        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            // SET WILDCARD VALUES
            prep.setString(1, name.trim());

            int rows = prep.executeUpdate();

            if (rows > 0) {
                System.out.println("Playlist '" + name + "' added successfully.\n");
                readAllPlaylists();
                return true;
            } else {
                System.out.println("Failed to add playlist.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Create Playlist: " + e.getMessage());
            return false;
        }
    }

    // UPDATE PLAYLIST
    public boolean updatePlaylist(String name, int id) {
        if (id <= 0) {
            System.out.println("Invalid playlist ID.");
            return false;
        }

        if (name == null || name.trim().isEmpty()) {
            System.out.println("Playlist name is required.");
            return false;
        }

        String query = "UPDATE playlists SET name = ? WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setString(1, name.trim());
            prep.setInt(2, id);

            int rows = prep.executeUpdate();

            if (rows > 0) {
                System.out.println("Playlist '" + name + "' updated successfully.\n");
                readAllPlaylists();
                return true;
            } else {
                System.out.println("Failed to update playlist.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Update Playlist: " + e.getMessage());
            return false;
        }
    }

    // ARCHIVE PLAYLIST
    public boolean archivePlaylist(int id) {
        if (id <= 0) {
            System.out.println("Invalid playlist ID.");
            return false;
        }

        String query = "UPDATE playlists SET is_archived = 1 WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            // SET WILDCARD VALUES
            prep.setInt(1, id);

            int rows = prep.executeUpdate();

            if (rows > 0) {
                System.out.println("Playlist ID " + id + " archived successfully.\n");
                readAllPlaylists();
                return true;
            } else {
                System.out.println("Failed to archive playlist.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Archive Playlist: " + e.getMessage());
            return false;
        }
    }

    // RESTORE PLAYLIST
    public boolean restorePlaylist(int id) {
        if (id <= 0) {
            System.out.println("Invalid playlist ID.");
            return false;
        }

        String query = "UPDATE playlists SET is_archived = 0 WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            // SET WILDCARD VALUES
            prep.setInt(1, id);

            int rows = prep.executeUpdate();

            if (rows > 0) {
                System.out.println("Playlist ID " + id + " restored successfully.\n");
                readAllPlaylists();
                return true;
            } else {
                System.out.println("Failed to restore playlist.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Restore Playlist: " + e.getMessage());
            return false;
        }
    }

    // DELETE PLAYLIST
    public boolean deletePlaylist(int id) {
        if (id <= 0) {
            System.out.println("Invalid playlist ID.");
            return false;
        }

        String query = "DELETE FROM playlists WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            // SET WILDCARD VALUES
            prep.setInt(1, id);

            int rows = prep.executeUpdate();

            if (rows > 0) {
                System.out.println("Playlist ID " + id + " deleted successfully.\n");
                readAllPlaylists();
                return true;
            } else {
                System.out.println("Failed to delete playlist.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Delete Playlist: " + e.getMessage());
            return false;
        }
    }

    // READ PLAYLIST BY ID
    public void readPlaylistById(int id) {
        String query = "SELECT * FROM playlists WHERE id = ?";

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
            System.err.println("Read Playlist By Id: " + e.getMessage());
        }
    }

    // SEARCH PLAYLIST BY NAME
    public void searchPlaylist(String keyword) {

        // VALIDATION
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("Search keyword cannot be empty.");
            return;
        }

        // DATABASE ACCESS LOGIC
        String query = "SELECT * FROM playlists WHERE name LIKE ?";

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
            System.err.println("Search Playlist: " + e.getMessage());
        }
    }

    // READ ARCHIVED PLAYLISTS
    public void readAllArchivedPlaylists() {
        String query = "SELECT * FROM playlists WHERE is_archived = 1";

        try (Connection conn = dbConnection.connect();
             Statement stmnt = conn.createStatement();
             ResultSet result = stmnt.executeQuery(query)) {

            System.out.println("+-------+----------------------+");
            System.out.printf("| %-5s | %-20s |%n", "ID", "Name");
            System.out.println("+-------+----------------------+");

            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");

                System.out.printf("| %-5d | %-20s |%n", id, name);
            }

            System.out.println("+-------+----------------------+");

        } catch (SQLException e) {
            System.err.println("Get All Archived Playlists: " + e.getMessage());
        }
    }
}
