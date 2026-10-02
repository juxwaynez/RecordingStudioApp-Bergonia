package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.model.Playlist;
import com.joysistvi.recordingapp.model.Song;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistRepositoryImpl implements PlaylistRepository {

    private final DbConnection dbConnection; // Composition

    // Constructor injection
    public PlaylistRepositoryImpl(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public List<Playlist> getAllPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        String query = "SELECT id, date_created, user_id FROM playlists";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query);
             ResultSet res = prep.executeQuery()) {

            while (res.next()) {
                playlists.add(new Playlist(
                        res.getInt("id"),
                        res.getString("date_created"),
                        res.getInt("user_id")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Read Playlists: " + e.getMessage());
        }

        return playlists;
    }

    @Override
    public List<Playlist> getPlaylistsByUser(int userId) {
        List<Playlist> playlists = new ArrayList<>();
        String query = "SELECT id, date_created, user_id FROM playlists WHERE user_id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, userId);
            ResultSet res = prep.executeQuery();

            while (res.next()) {
                playlists.add(new Playlist(
                        res.getInt("id"),
                        res.getString("date_created"),
                        res.getInt("user_id")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Read Playlists By User: " + e.getMessage());
        }

        return playlists;
    }

    @Override
    public boolean createPlaylist(int userId) {
        String query = "INSERT INTO playlists (user_id, date_created) VALUES (?, CURDATE())"; // Anti-SQL Injection

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, userId);

            int rowsAffected = prep.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Error in creating playlist: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean deletePlaylist(int id) {
        String deleteSongsQuery = "DELETE FROM playlist_songs WHERE playlist_id = ?";
        String deletePlaylistQuery = "DELETE FROM playlists WHERE id = ?";

        try (Connection conn = dbConnection.connect()) {

            // Remove the junction rows first to avoid a foreign key conflict
            try (PreparedStatement prepSongs = conn.prepareStatement(deleteSongsQuery)) {
                prepSongs.setInt(1, id);
                prepSongs.executeUpdate();
            }

            try (PreparedStatement prepPlaylist = conn.prepareStatement(deletePlaylistQuery)) {
                prepPlaylist.setInt(1, id);
                int rows = prepPlaylist.executeUpdate();
                return rows > 0;
            }

        } catch (SQLException e) {
            System.out.println("Delete Playlist: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<Song> getSongsInPlaylist(int playlistId) {
        List<Song> songs = new ArrayList<>();
        String query = "SELECT s.id, s.title, s.length, s.genre, a.name " +
                "FROM playlist_songs ps " +
                "JOIN songs s ON ps.song_id = s.id " +
                "JOIN albums a ON s.album_id = a.id " +
                "WHERE ps.playlist_id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, playlistId);
            ResultSet res = prep.executeQuery();

            while (res.next()) {
                songs.add(new Song(
                        res.getInt("id"),
                        res.getString("title"),
                        res.getString("length"),
                        res.getString("genre"),
                        res.getString("name") // album name
                ));
            }

        } catch (SQLException e) {
            System.out.println("Read Songs In Playlist: " + e.getMessage());
        }

        return songs;
    }

    @Override
    public boolean addSongToPlaylist(int playlistId, int songId) {
        String query = "INSERT INTO playlist_songs (playlist_id, song_id) VALUES (?,?)";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, playlistId);
            prep.setInt(2, songId);

            int rowsAffected = prep.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Add Song To Playlist: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean removeSongFromPlaylist(int playlistId, int songId) {
        return false;
    }


}