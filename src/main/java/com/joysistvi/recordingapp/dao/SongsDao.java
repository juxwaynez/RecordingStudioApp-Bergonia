package com.joysistvi.recordingapp.dao;

import com.joysistvi.recordingapp.config.DbConnection;

import java.sql.*;

public class SongsDao {

    // COMPOSITION
    private final DbConnection dbConnection;

    // CONSTRUCTOR INJECTION
    public SongsDao(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    // CRUD OPERATION: READ ALL SONGS
    public void readAllSongs() {
        String query = "SELECT * FROM songs WHERE is_archived = 0";

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
            System.err.println("Get All Songs: " + e.getMessage());
        }
    }

    // CREATE SONG
    public boolean createSong(String title, int artistId) {

        // VALIDATION
        if (title == null || title.trim().isEmpty()) {
            System.out.println("Song title is required.");
            return false;
        }

        if (artistId <= 0) {
            System.out.println("Invalid artist ID.");
            return false;
        }

        String query = "INSERT INTO songs (title, artist_id) VALUES (?, ?)";
        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setString(1, title.trim());
            prep.setInt(2, artistId);

            int rows = prep.executeUpdate();

            if (rows > 0) {
                System.out.println("Song \"" + title + "\" added successfully.\n");
                readAllSongs();
                return true;
            } else {
                System.out.println("Failed to add song.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Create Song: " + e.getMessage());
            return false;
        }
    }

    // UPDATE SONG
    public boolean updateSong(int id, String title, int artistId) {
        if (id <= 0) {
            System.out.println("Invalid song ID.");
            return false;
        }

        if (title == null || title.trim().isEmpty()) {
            System.out.println("Song title is required.");
            return false;
        }

        if (artistId <= 0) {
            System.out.println("Invalid artist ID.");
            return false;
        }

        String query = "UPDATE songs SET title = ?, artist_id = ? WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setString(1, title.trim());
            prep.setInt(2, artistId);
            prep.setInt(3, id);

            int rows = prep.executeUpdate();

            if (rows > 0) {
                System.out.println("Song ID " + id + " updated successfully.\n");
                readAllSongs();
                return true;
            } else {
                System.out.println("Failed to update song.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Update Song: " + e.getMessage());
            return false;
        }
    }

    // ARCHIVE SONG
    public boolean archiveSong(int id) {
        if (id <= 0) {
            System.out.println("Invalid song ID.");
            return false;
        }

        String query = "UPDATE songs SET is_archived = 1 WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, id);

            int rows = prep.executeUpdate();

            if (rows > 0) {
                System.out.println("Song ID " + id + " archived successfully.\n");
                readAllSongs();
                return true;
            } else {
                System.out.println("Failed to archive song.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Archive Song: " + e.getMessage());
            return false;
        }
    }

    // RESTORE SONG
    public boolean restoreSong(int id) {
        if (id <= 0) {
            System.out.println("Invalid song ID.");
            return false;
        }

        String query = "UPDATE songs SET is_archived = 0 WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, id);

            int rows = prep.executeUpdate();

            if (rows > 0) {
                System.out.println("Song ID " + id + " restored successfully.\n");
                readAllSongs();
                return true;
            } else {
                System.out.println("Failed to restore song.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Restore Song: " + e.getMessage());
            return false;
        }
    }

    // DELETE SONG
    public boolean deleteSong(int id) {
        if (id <= 0) {
            System.out.println("Invalid song ID.");
            return false;
        }

        String query = "DELETE FROM songs WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, id);

            int rows = prep.executeUpdate();

            if (rows > 0) {
                System.out.println("Song ID " + id + " deleted successfully.\n");
                readAllSongs();
                return true;
            } else {
                System.out.println("Failed to delete song.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Delete Song: " + e.getMessage());
            return false;
        }
    }

    // READ SONG BY ID
    public void readSongById(int id) {
        String query = "SELECT * FROM songs WHERE id = ?";

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
            System.out.println("Read Song By Id: " + e.getMessage());
        }
    }

    // SEARCH SONG BY TITLE
    public void searchSong(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("Search keyword cannot be empty.");
            return;
        }

        String query = "SELECT * FROM songs WHERE title LIKE ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setString(1, "%" + keyword.trim() + "%");
            ResultSet res = prep.executeQuery();

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
            System.out.println("Search Song: " + e.getMessage());
        }
    }

    // READ ALL ARCHIVED SONGS
    public void readAllArchivedSongs() {
        String query = "SELECT * FROM songs WHERE is_archived = 1";

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
            System.err.println("Get All Archived Songs: " + e.getMessage());
        }
    }
}