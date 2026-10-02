package com.joysistvi.recordingapp.controller;

import com.joysistvi.recordingapp.model.Song;
import com.joysistvi.recordingapp.service.SongService;

import java.util.List;

public class SongController {

    private final SongService songService; // Composition

    // Constructor injection
    public SongController(SongService songService) {
        this.songService = songService;
    }

    public List<Song> handleViewAllSongs() {
        return songService.getAllSongs();
    }

    public Song handleGetSongById(int id) {
        return songService.getSongById(id);
    }

    public List<Song> handleViewArchivedSongs() {
        return songService.getArchivedSongs();
    }

    public List<Song> handleSearchSong(String keyword) {
        return songService.searchSong(keyword);
    }

    public boolean handleAddSong(Song song) {
        return songService.addSong(song);
    }

    public boolean handleUpdateSong(Song song) {
        return songService.updateSong(song);
    }

    public boolean handleDeleteSong(int id) {
        return songService.deleteSong(id);
    }

    public boolean handleArchiveSong(int id) {
        return songService.archiveSong(id);
    }

    public boolean handleRestoreSong(int id) {
        return songService.restoreSong(id);
    }
}