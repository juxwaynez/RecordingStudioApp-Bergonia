package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.dao.AlbumsDao;

import java.sql.*;

public class AlbumsRepoImpl implements AlbumsDao {

    // COMPOSITION
    private final DbConnection dbConnection;

    // CONSTRUCTOR INJECTION
    public AlbumsRepoImpl(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    // READ ALL ALBUMS
    @Override
    public void readAllAlbums() {

        String query = "SELECT * FROM albums WHERE is_archived = 0";

        try (Connection conn = dbConnection.connect();
             Statement stmnt = conn.createStatement();
             ResultSet result = stmnt.executeQuery(query)) {

            // TOP BORDER
            System.out.println("+-------+----------------------+");

            // HEADER
            System.out.printf("| %-5s | %-20s |%n", "ID", "Title");

            // HEADER DIVIDER
            System.out.println("+-------+----------------------+");

            // DISPLAY ALBUMS
            while (result.next()) {

                int id = result.getInt("id");
                String title = result.getString("title");

                System.out.printf("| %-5d | %-20s |%n", id, title);
            }

            // BOTTOM BORDER
            System.out.println("+-------+----------------------+");

        } catch (SQLException e) {
            System.err.println("Get All Albums: " + e.getMessage());
        }
    }

    // CREATE ALBUM
    @Override
    public boolean createAlbum(String title) {

        // VALIDATION
        if (title == null || title.trim().isEmpty()) {
            System.out.println("Album title is required");
            return false;
        }

        String query = "INSERT INTO albums (title) VALUES (?)";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            // SET WILDCARD VALUE
            prep.setString(1, title.trim());

            int rows = prep.executeUpdate();

            if (rows > 0) {

                System.out.println(
                        "Album " + title + " added successfully.\n"
                );

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
    @Override
    public boolean updateAlbum(String title, int id) {

        // VALIDATION
        if (id <= 0) {
            System.out.println("Invalid album ID.");
            return false;
        }

        if (title == null || title.trim().isEmpty()) {
            System.out.println("Album title is required");
            return false;
        }

        String query = "UPDATE albums SET title = ? WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            // SET WILDCARD VALUES
            prep.setString(1, title.trim());
            prep.setInt(2, id);

            int rows = prep.executeUpdate();

            if (rows > 0) {

                System.out.println(
                        "Album " + title + " updated successfully.\n"
                );

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
    @Override
    public boolean archiveAlbum(int id) {

        if (id <= 0) {
            System.out.println("Invalid album ID.");
            return false;
        }

        String query =
                "UPDATE albums SET is_archived = 1 WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, id);

            int rows = prep.executeUpdate();

            if (rows > 0) {

                System.out.println(
                        "Album " + id + " archived successfully.\n"
                );

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
    @Override
    public boolean restoreAlbum(int id) {

        if (id <= 0) {
            System.out.println("Invalid album ID.");
            return false;
        }

        String query =
                "UPDATE albums SET is_archived = 0 WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, id);

            int rows = prep.executeUpdate();

            if (rows > 0) {

                System.out.println(
                        "Album " + id + " restored successfully.\n"
                );

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
    @Override
    public boolean deleteAlbum(int id) {

        if (id <= 0) {
            System.out.println("Invalid album ID.");
            return false;
        }

        String query = "DELETE FROM albums WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, id);

            int rows = prep.executeUpdate();

            if (rows > 0) {

                System.out.println(
                        "Album " + id + " deleted successfully.\n"
                );

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
    @Override
    public void readAlbumById(int id) {

        String query = "SELECT * FROM albums WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, id);

            ResultSet res = prep.executeQuery();

            System.out.println("+-------+----------------------+");
            System.out.printf("| %-5s | %-20s |%n", "ID", "Title");
            System.out.println("+-------+----------------------+");

            if (res.next()) {

                System.out.printf(
                        "| %-5d | %-20s |%n",
                        res.getInt("id"),
                        res.getString("title")
                );
            }

            System.out.println("+-------+----------------------+");

        } catch (SQLException e) {

            System.err.println(
                    "Read Album By ID: " + e.getMessage()
            );
        }
    }

    // SEARCH ALBUM
    @Override
    public void searchAlbum(String keyword) {

        // VALIDATION
        if (keyword == null || keyword.trim().isEmpty()) {

            System.out.println("Search keyword cannot be empty.");
            return;
        }

        String query =
                "SELECT * FROM albums WHERE title LIKE ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setString(
                    1,
                    "%" + keyword.trim() + "%"
            );

            ResultSet res = prep.executeQuery();

            // UI / VIEW
            System.out.println("+-------+----------------------+");
            System.out.printf("| %-5s | %-20s |%n", "ID", "Title");
            System.out.println("+-------+----------------------+");

            while (res.next()) {

                System.out.printf(
                        "| %-5d | %-20s |%n",
                        res.getInt("id"),
                        res.getString("title")
                );
            }

            System.out.println("+-------+----------------------+");

        } catch (SQLException e) {

            System.err.println(
                    "Search Album: " + e.getMessage()
            );
        }
    }

    // READ ALL ARCHIVED ALBUMS
    @Override
    public void readAllArchivedAlbums() {

        String query =
                "SELECT * FROM albums WHERE is_archived = 1";

        try (Connection conn = dbConnection.connect();
             Statement stmnt = conn.createStatement();
             ResultSet result = stmnt.executeQuery(query)) {

            // TOP BORDER
            System.out.println("+-------+----------------------+");

            // HEADER
            System.out.printf(
                    "| %-5s | %-20s |%n",
                    "ID",
                    "Title"
            );

            // HEADER DIVIDER
            System.out.println("+-------+----------------------+");

            while (result.next()) {

                int id = result.getInt("id");
                String title = result.getString("title");

                System.out.printf(
                        "| %-5d | %-20s |%n",
                        id,
                        title
                );
            }

            // BOTTOM BORDER
            System.out.println("+-------+----------------------+");

        } catch (SQLException e) {

            System.err.println(
                    "Get All Archived Albums: "
                            + e.getMessage()
            );
        }
    }
}
