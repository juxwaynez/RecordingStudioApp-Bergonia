package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.model.Album;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlbumRepositoryImpl implements AlbumRepository {

    private final DbConnection dbConnection; // Composition

    // Constructor injection
    public AlbumRepositoryImpl(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public List<Album> getAllAlbumsWithArtist() {
        List<Album> albums = new ArrayList<>();
        String query = "SELECT al.id, al.name, al.year, ar.name AS artist_name " +
                "FROM albums al " +
                "JOIN artists ar ON al.artist_id = ar.id";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query);
             ResultSet res = prep.executeQuery()) {

            while (res.next()) {
                albums.add(new Album(
                        res.getInt("id"),
                        res.getString("name"),
                        res.getInt("year"),
                        res.getString("artist_name")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Read Albums With Artist: " + e.getMessage());
        }

        return albums;
    }

    @Override
    public List<Album> searchAlbum(String keyword) {
        List<Album> albums = new ArrayList<>();
        String query = "SELECT al.id, al.name, al.year, ar.name AS artist_name " +
                "FROM albums al " +
                "JOIN artists ar ON al.artist_id = ar.id " +
                "WHERE al.name LIKE ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setString(1, "%" + keyword + "%"); // wildcard search
            ResultSet res = prep.executeQuery();

            while (res.next()) {
                albums.add(new Album(
                        res.getInt("id"),
                        res.getString("name"),
                        res.getInt("year"),
                        res.getString("artist_name")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Search Album: " + e.getMessage());
        }

        return albums;
    }

    @Override
    public boolean createAlbum(Album album) {
        String query = "INSERT INTO albums (name, year, artist_id) VALUES (?,?,?)"; // Anti-SQL Injection

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setString(1, album.getName());
            prep.setInt(2, album.getYear());
            prep.setInt(3, album.getArtistId());

            int rowsAffected = prep.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Error in inserting album: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateAlbum(Album album) {
        String query = "UPDATE albums SET name = ?, year = ?, artist_id = ? WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setString(1, album.getName());
            prep.setInt(2, album.getYear());
            prep.setInt(3, album.getArtistId());
            prep.setInt(4, album.getId());

            int rowsAffected = prep.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Update Album: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean deleteAlbum(int id) {
        String query = "DELETE FROM albums WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, id);
            int rows = prep.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Delete Album: " + e.getMessage());
        }
        return false;
    }
}