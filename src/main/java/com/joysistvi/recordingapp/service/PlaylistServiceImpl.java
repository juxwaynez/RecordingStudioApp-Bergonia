package com.joysistvi.recordingapp.service;

import com.joysistvi.recordingapp.model.Playlist;
import com.joysistvi.recordingapp.model.Song;
import com.joysistvi.recordingapp.repository.PlaylistRepository;

import java.util.List;

public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository playlistRepository; // Composition

    // Constructor injection
    public PlaylistServiceImpl(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    @Override
    public List<Playlist> getAllPlaylists() {
        return playlistRepository.getAllPlaylists();
    }

    @Override
    public List<Playlist> getPlaylistsByUser(int userId) {
        if (userId <= 0) {
            System.out.println("Invalid user ID.");
            return List.of();
        }
        return playlistRepository.getPlaylistsByUser(userId);
    }

    @Override
    public boolean createPlaylist(int userId) {
        if (userId <= 0) {
            System.out.println("A valid user ID is required.");
            return false;
        }
        return playlistRepository.createPlaylist(userId);
    }

    @Override
    public boolean deletePlaylist(int id) {
        if (id <= 0) {
            System.out.println("Invalid playlist ID.");
            return false;
        }
        return playlistRepository.deletePlaylist(id);
    }

    @Override
    public List<Song> getSongsInPlaylist(int playlistId) {
        if (playlistId <= 0) {
            System.out.println("Invalid playlist ID.");
            return List.of();
        }
        return playlistRepository.getSongsInPlaylist(playlistId);
    }

    @Override
    public boolean addSongToPlaylist(int playlistId, int songId) {
        if (playlistId <= 0 || songId <= 0) {
            System.out.println("A valid playlist ID and song ID are required.");
            return false;
        }
        return playlistRepository.addSongToPlaylist(playlistId, songId);
    }

    @Override
    public boolean removeSongFromPlaylist(int playlistId, int songId) {
        if (playlistId <= 0 || songId <= 0) {
            System.out.println("A valid playlist ID and song ID are required.");
            return false;
        }
        return playlistRepository.removeSongFromPlaylist(playlistId, songId);
    }
}