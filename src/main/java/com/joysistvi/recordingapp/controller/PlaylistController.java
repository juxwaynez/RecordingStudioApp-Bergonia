package com.joysistvi.recordingapp.controller;

import com.joysistvi.recordingapp.model.Playlist;
import com.joysistvi.recordingapp.model.Song;
import com.joysistvi.recordingapp.service.PlaylistService;

import java.util.List;

public class PlaylistController {

    private final PlaylistService playlistService; // Composition

    // Constructor injection
    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    public List<Playlist> handleViewAllPlaylists() {
        return playlistService.getAllPlaylists();
    }

    public List<Playlist> handleViewPlaylistsByUser(int userId) {
        return playlistService.getPlaylistsByUser(userId);
    }

    public boolean handleCreatePlaylist(int userId) {
        return playlistService.createPlaylist(userId);
    }

    public boolean handleDeletePlaylist(int id) {
        return playlistService.deletePlaylist(id);
    }

    public List<Song> handleViewSongsInPlaylist(int playlistId) {
        return playlistService.getSongsInPlaylist(playlistId);
    }

    public boolean handleAddSongToPlaylist(int playlistId, int songId) {
        return playlistService.addSongToPlaylist(playlistId, songId);
    }

    public boolean handleRemoveSongFromPlaylist(int playlistId, int songId) {
        return playlistService.removeSongFromPlaylist(playlistId, songId);
    }
}