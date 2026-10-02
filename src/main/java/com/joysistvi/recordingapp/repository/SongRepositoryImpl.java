package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.model.Song;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SongRepositoryImpl implements SongRepository{

    private final DbConnection dbConnection; // Composition

    // Constructor injection
    public SongRepositoryImpl(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }


    @Override
    public List<Song> getAllSongsWithAlbum() {
        List<Song> songs = new ArrayList<>();
        String query = "SELECT s.id, s.title, s.length, s.genre, a.name " +
                "FROM songs s " +
                "JOIN albums a ON s.album_id = a.id " +
                "WHERE s.is_archived = 0";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query);
             ResultSet res = prep.executeQuery()) {

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
            System.out.println("Read Songs With Album: " + e.getMessage());
        }

        return songs;
    }

    @Override
    public Song getSongById(int id) {

        String query = """
        SELECT
            s.id,
            s.title,
            s.length,
            s.genre,
            s.album_id,
            a.name AS album_name
        FROM songs s
        INNER JOIN albums a
            ON s.album_id = a.id
        WHERE s.id = ?
        AND s.is_archived = false
    """;

        try (
                Connection conn = dbConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(query)
        ) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                Song song = new Song();

                song.setId(rs.getInt("id"));
                song.setTitle(rs.getString("title"));
                song.setLength(rs.getString("length"));
                song.setGenre(rs.getString("genre"));
                song.setAlbumId(rs.getInt("album_id"));
                song.setAlbumName(rs.getString("album_name"));

                return song;
            }

        } catch (SQLException e) {
            System.out.println("Get Song By ID Error: " + e.getMessage());
        }

        return null;
    }

    // Search Song by title (case-insensitive)
    public List<Song> searchSong(String keyword) {
        List<Song> songs = new ArrayList<>();
        String query = "SELECT s.id, s.title, s.length, s.genre, a.name " +
                "FROM songs s " +
                "JOIN albums a ON s.album_id = a.id " +
                "WHERE s.is_archived = 0 AND s.title LIKE ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setString(1, "%" + keyword + "%"); // wildcard search
            ResultSet res = prep.executeQuery();

            while (res.next()) {
                songs.add(new Song(
                        res.getInt("id"),
                        res.getString("title"),
                        res.getString("length"),
                        res.getString("genre"),
                        res.getString("name")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Search Song: " + e.getMessage());
        }
        return songs;
    }

    @Override
    public List<Song> readArchivedSong() {
        List<Song> songs = new ArrayList<>();
        String query = "SELECT s.id, s.title, s.length, s.genre, a.name, s.album_id " +
                "FROM songs s " +
                "JOIN albums a ON s.album_id = a.id " +
                "WHERE s.is_archived = 1";

        try (Connection connection = dbConnection.connect();
             PreparedStatement prep = connection.prepareStatement(query);
             ResultSet result = prep.executeQuery()) {

            while (result.next()) {
                songs.add(new Song(
                        result.getInt("id"),
                        result.getString("title"),
                        result.getString("length"),
                        result.getString("genre"),
                        result.getString("name") // album name
                ));
            }

        } catch (SQLException e) {
            System.out.println("Read Archived Songs: " + e.getMessage());
        }

        return songs;
    }


    @Override
    public boolean createSong(Song song) {
        String query = "INSERT INTO songs (title, length, genre, album_id) " + // create statement
                "VALUES (?,?,?,?)"; // Anti-SQL Injection

        // Try-with-resources: automatically close opened connection
        try (Connection connection = dbConnection.connect();
             PreparedStatement prep = connection.prepareStatement(query);
        ) {
            // Bind values to the placeholders in the query
            prep.setString(1, song.getTitle());
            prep.setString(2, song.getLength());
            prep.setString(3, song.getGenre());
            prep.setInt(4, song.getAlbumId());

            // Execute the insert statement
            int rowsAffected = prep.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            // Print the error message if something goes wrong
            System.out.println("Error in inserting song: " + e.getMessage());

        }
        return false;
    }

    // Update Song
    @Override
    public boolean updateSong(Song song) {

        String query = """
            UPDATE songs
            SET
                title = ?,
                length = ?,
                genre = ?,
                album_id = ?
            WHERE id = ?
            """;

        try (
                Connection conn = dbConnection.connect();
                PreparedStatement prep = conn.prepareStatement(query)
        ) {

            prep.setString(1, song.getTitle());
            prep.setString(2, song.getLength());
            prep.setString(3, song.getGenre());
            prep.setInt(4, song.getAlbumId());
            prep.setInt(5, song.getId());

            int rowsAffected = prep.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Update Song: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }


    // Hard Delete Song
    @Override
    public boolean deleteSong(int id) {
        String query = "DELETE FROM songs WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, id);
            int rows = prep.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Delete Song: " + e.getMessage());
        }
        return false;
    }


    @Override
    // Archive Song (soft delete)
    public boolean archiveSong(int id) {
        String query = "UPDATE songs SET is_archived = 1 WHERE id = ?";
        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, id);
            int rows = prep.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Archive Song: " + e.getMessage());
        }
        return false;
    }

    // Restore Song (un-archive)
    public boolean restoreSong(int id) {
        String query = "UPDATE songs SET is_archived = 0 WHERE id = ?";
        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, id);
            int rows = prep.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Restore Song: " + e.getMessage());
        }
        return false;
    }




    public List<Song> getAllSongs() {
        List<Song> songs = new ArrayList<>();
        String query = "SELECT s.id, s.title, s.length, s.genre, a.name " +
                "FROM songs s " +
                "JOIN albums a ON s.album_id = a.id " +
                "WHERE s.is_archived = 0";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query);
             ResultSet res = prep.executeQuery()) {


            while (res.next()) {
                songs.add(new Song(
                        res.getInt("id"),
                        res.getString("title"),
                        res.getString("length"),
                        res.getString("genre"),
                        res.getString("name")
                ));

            }

        } catch (Exception e) {
            System.out.println("Read Songs With Album: " + e.getMessage());
        }

        return songs;
    }


}