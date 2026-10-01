package com.joysistvi.recordingapp.dao;

import com.joysistvi.recordingapp.config.DbConnection;

import java.sql.*;

public class AlbumsDao {

    // COMPOSITION
    private final DbConnection dbConnection;

    // CONSTRUCTOR INJECTION
    public AlbumsDao(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    // READ ALL ALBUMS
    public void readAllAlbums() {
        String query = "SELECT * FROM albums WHERE is_archived = 0";

        // TRY-WITH-RESOURCES FOR AUTO-CLOSING DATABASE CONNECTIONS
        try (Connection conn = dbConnection.connect();
             Statement stmnt = conn.createStatement();
             ResultSet result = stmnt.executeQuery(query)) {

            // TOP BORDER
            System.out.println("+-------+----------------------+-----------+");
            // HEADER
            System.out.printf("| %-5s | %-20s | %-9s |%n", "ID", "Title", "Artist ID");
            // HEADER DIVIDER
            System.out.println("+-------+----------------------+-----------+");

            // EXTRACT AND DISPLAY ROWS
            while (result.next()) {
                int id = result.getInt("id");
                String title = result.getString("title");
                int artistId = result.getInt("artist_id");

                System.out.printf("| %-5d | %-20s | %-9d |%n", id, title, artistId);
            }

            // BOTTOM BORDER
            System.out.println("+-------+----------------------+-----------+");

        } catch (SQLException e) {
            System.err.println("Get All Albums: " + e.getMessage());
        }
    }

    // CREATE ALBUM
    public boolean createAlbums (String title, int artistId) {

        // VALIDATION
        if (title == null || title.trim().isEmpty()) {
            System.out.println("Album title is required.");
            return false;
        }

        if (artistId <= 0) {
            System.out.println("Invalid artist ID.");
            return false;
        }

        // PARAMETERIZED QUERY
        String query = "INSERT INTO albums (title, artist_id) VALUES (?, ?)";
        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            // SET WILDCARD VALUES
            prep.setString(1, title.trim());
            prep.setInt(2, artistId);

            int rows = prep.executeUpdate();

            if (rows > 0) {
                System.out.println("Album '" + title + "' added successfully.\n");
                readAllAlbums();
                return true;
            } else {
                System.out.println("Failed to add album.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Create Album: " + e.getMessage());
            return false;
        }
    }

    // UPDATE ALBUM
    public boolean updateAlbum(String title, int artistId, int id) {
        if (id <= 0) {
            System.out.println("Invalid album ID.");
            return false;
        }

        if (title == null || title.trim().isEmpty()) {
            System.out.println("Album title is required.");
            return false;
        }

        if (artistId <= 0) {
            System.out.println("Invalid artist ID.");
            return false;
        }

        String query = "UPDATE albums SET title = ?, artist_id = ? WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setString(1, title.trim());
            prep.setInt(2, artistId);
            prep.setInt(3, id);

            int rows = prep.executeUpdate();

            if (rows > 0) {
                System.out.println("Album '" + title + "' updated successfully.\n");
                readAllAlbums();
                return true;
            } else {
                System.out.println("Failed to update album.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Update Album: " + e.getMessage());
            return false;
        }
    }

    // ARCHIVE ALBUM
    public boolean archiveAlbum(int id) {
        if (id <= 0) {
            System.out.println("Invalid album ID.");
            return false;
        }

        String query = "UPDATE albums SET is_archived = 1 WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            // SET WILDCARD VALUES
            prep.setInt(1, id);

            int rows = prep.executeUpdate();

            if (rows > 0) {
                System.out.println("Album ID " + id + " archived successfully.\n");
                readAllAlbums();
                return true;
            } else {
                System.out.println("Failed to archive album.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Archive Album: " + e.getMessage());
            return false;
        }
    }

    // RESTORE ALBUM
    public boolean restoreAlbum(int id) {
        if (id <= 0) {
            System.out.println("Invalid album ID.");
            return false;
        }

        String query = "UPDATE albums SET is_archived = 0 WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            // SET WILDCARD VALUES
            prep.setInt(1, id);

            int rows = prep.executeUpdate();

            if (rows > 0) {
                System.out.println("Album ID " + id + " restored successfully.\n");
                readAllAlbums();
                return true;
            } else {
                System.out.println("Failed to restore album.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Restore Album: " + e.getMessage());
            return false;
        }
    }

    // DELETE ALBUM
    public boolean deleteAlbum(int id) {
        if (id <= 0) {
            System.out.println("Invalid album ID.");
            return false;
        }

        String query = "DELETE FROM albums WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            // SET WILDCARD VALUES
            prep.setInt(1, id);

            int rows = prep.executeUpdate();

            if (rows > 0) {
                System.out.println("Album ID " + id + " deleted successfully.\n");
                readAllAlbums();
                return true;
            } else {
                System.out.println("Failed to delete album.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Delete Album: " + e.getMessage());
            return false;
        }
    }

    // READ ALBUM BY ID
    public void readAlbumById(int id) {
        String query = "SELECT * FROM albums WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, id);
            ResultSet res = prep.executeQuery();

            System.out.println("+-------+----------------------+-----------+");
            System.out.printf("| %-5s | %-20s | %-9s |%n", "ID", "Title", "Artist ID");
            System.out.println("+-------+----------------------+-----------+");

            if (res.next()) {
                System.out.printf("| %-5d | %-20s | %-9d |%n",
                        res.getInt("id"),
                        res.getString("title"),
                        res.getInt("artist_id"));
            }

            System.out.println("+-------+----------------------+-----------+");

        } catch (SQLException e) {
            System.err.println("Read Album By Id: " + e.getMessage());
        }
    }

    // SEARCH ALBUM BY TITLE
    public void searchAlbum(String keyword) {

        // VALIDATION
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("Search keyword cannot be empty.");
            return;
        }

        // DATABASE ACCESS LOGIC
        String query = "SELECT * FROM albums WHERE title LIKE ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setString(1, "%" + keyword.trim() + "%");
            ResultSet res = prep.executeQuery();

            // UI / VIEW
            System.out.println("+-------+----------------------+-----------+");
            System.out.printf("| %-5s | %-20s | %-9s |%n", "ID", "Title", "Artist ID");
            System.out.println("+-------+----------------------+-----------+");

            while (res.next()) {
                System.out.printf("| %-5d | %-20s | %-9d |%n",
                        res.getInt("id"),
                        res.getString("title"),
                        res.getInt("artist_id"));
            }

            System.out.println("+-------+----------------------+-----------+");

        } catch (SQLException e) {
            System.err.println("Search Album: " + e.getMessage());
        }
    }

    // READ ARCHIVED ALBUMS
    public void readAllArchivedAlbums() {
        String query = "SELECT * FROM albums WHERE is_archived = 1";

        try (Connection conn = dbConnection.connect();
             Statement stmnt = conn.createStatement();
             ResultSet result = stmnt.executeQuery(query)) {

            System.out.println("+-------+----------------------+-----------+");
            System.out.printf("| %-5s | %-20s | %-9s |%n", "ID", "Title", "Artist ID");
            System.out.println("+-------+----------------------+-----------+");

            while (result.next()) {
                int id = result.getInt("id");
                String title = result.getString("title");
                int artistId = result.getInt("artist_id");

                System.out.printf("| %-5d | %-20s | %-9d |%n", id, title, artistId);
            }

            System.out.println("+-------+----------------------+-----------+");

        } catch (SQLException e) {
            System.err.println("Get All Archived Albums: " + e.getMessage());
        }
    }
}