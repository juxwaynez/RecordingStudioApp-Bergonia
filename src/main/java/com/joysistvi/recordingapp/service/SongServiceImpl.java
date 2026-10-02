package com.joysistvi.recordingapp.service;

import com.joysistvi.recordingapp.model.Song;
import com.joysistvi.recordingapp.repository.SongRepository;

import java.util.List;

public class SongServiceImpl implements SongService {

    private final SongRepository songRepository; // Composition

    // Constructor injection
    public SongServiceImpl(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    @Override
    public List<Song> getAllSongs() {
        return songRepository.getAllSongsWithAlbum();
    }

    @Override
    public Song getSongById(int id) {
        return songRepository.getSongById(id);
    }

    @Override
    public List<Song> searchSong(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("Search keyword cannot be empty.");
            return List.of();
        }
        return songRepository.searchSong(keyword.trim());
    }

    @Override
    public List<Song> getArchivedSongs() {
        return songRepository.readArchivedSong();
    }

    @Override
    public boolean addSong(Song song) {
        if (!isValid(song)) {
            return false;
        }
        return songRepository.createSong(song);
    }

    @Override
    public boolean updateSong(Song song) {
        if (song.getId() <= 0) {
            System.out.println("Invalid song ID.");
            return false;
        }
        if (!isValid(song)) {
            return false;
        }
        return songRepository.updateSong(song);
    }

    @Override
    public boolean deleteSong(int id) {
        if (id <= 0) {
            System.out.println("Invalid song ID.");
            return false;
        }
        return songRepository.deleteSong(id);
    }

    @Override
    public boolean archiveSong(int id) {
        if (id <= 0) {
            System.out.println("Invalid song ID.");
            return false;
        }
        return songRepository.archiveSong(id);
    }

    @Override
    public boolean restoreSong(int id) {
        if (id <= 0) {
            System.out.println("Invalid song ID.");
            return false;
        }
        return songRepository.restoreSong(id);
    }

    // Simple validation rules before hitting the database
    private boolean isValid(Song song) {
        if (song.getTitle() == null || song.getTitle().trim().isEmpty()) {
            System.out.println("Song title is required.");
            return false;
        }
        if (song.getLength() == null || song.getLength().trim().isEmpty()) {
            System.out.println("Song length is required.");
            return false;
        }
        if (song.getGenre() == null || song.getGenre().trim().isEmpty()) {
            System.out.println("Song genre is required.");
            return false;
        }
        if (song.getAlbumId() <= 0) {
            System.out.println("A valid album ID is required.");
            return false;
        }
        return true;
    }
}