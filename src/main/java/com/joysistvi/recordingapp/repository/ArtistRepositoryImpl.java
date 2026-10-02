package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.model.Artist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArtistRepositoryImpl implements ArtistRepository {

    private final DbConnection dbConnection; // Composition

    // Constructor injection
    public ArtistRepositoryImpl(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public List<Artist> getAllArtists() {
        List<Artist> artists = new ArrayList<>();
        String query = "SELECT id, name FROM artists WHERE is_archived = 0";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query);
             ResultSet res = prep.executeQuery()) {

            while (res.next()) {
                artists.add(new Artist(
                        res.getInt("id"),
                        res.getString("name")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Read Artists: " + e.getMessage());
        }

        return artists;
    }

    @Override
    public List<Artist> searchArtist(String keyword) {
        List<Artist> artists = new ArrayList<>();
        String query = "SELECT id, name FROM artists WHERE is_archived = 0 AND name LIKE ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setString(1, "%" + keyword + "%"); // wildcard search
            ResultSet res = prep.executeQuery();

            while (res.next()) {
                artists.add(new Artist(
                        res.getInt("id"),
                        res.getString("name")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Search Artist: " + e.getMessage());
        }

        return artists;
    }

    @Override
    public Artist getArtistById(int id) {
        String query = "SELECT id, name FROM artists WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, id);
            ResultSet res = prep.executeQuery();

            if (res.next()) {
                return new Artist(res.getInt("id"), res.getString("name"));
            }

        } catch (SQLException e) {
            System.out.println("Read Artist By Id: " + e.getMessage());
        }

        return null; // not found
    }

    @Override
    public List<Artist> readArchivedArtist() {
        List<Artist> artists = new ArrayList<>();
        String query = "SELECT id, name FROM artists WHERE is_archived = 1";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query);
             ResultSet res = prep.executeQuery()) {

            while (res.next()) {
                artists.add(new Artist(
                        res.getInt("id"),
                        res.getString("name")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Read Archived Artists: " + e.getMessage());
        }

        return artists;
    }

    @Override
    public boolean createArtist(Artist artist) {
        String query = "INSERT INTO artists (name) VALUES (?)"; // Anti-SQL Injection

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setString(1, artist.getName());

            int rowsAffected = prep.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Error in inserting artist: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateArtist(Artist artist) {
        String query = "UPDATE artists SET name = ? WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setString(1, artist.getName());
            prep.setInt(2, artist.getId());

            int rowsAffected = prep.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Update Artist: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean deleteArtist(int id) {
        String query = "DELETE FROM artists WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, id);
            int rows = prep.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Delete Artist: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean archiveArtist(int id) {
        String query = "UPDATE artists SET is_archived = 1 WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, id);
            int rows = prep.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Archive Artist: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean restoreArtist(int id) {
        String query = "UPDATE artists SET is_archived = 0 WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, id);
            int rows = prep.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Restore Artist: " + e.getMessage());
        }
        return false;
    }
}