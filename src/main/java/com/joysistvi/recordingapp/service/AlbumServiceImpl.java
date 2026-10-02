package com.joysistvi.recordingapp.service;

import com.joysistvi.recordingapp.model.Album;
import com.joysistvi.recordingapp.repository.AlbumRepository;

import java.time.Year;
import java.util.List;

public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository albumRepository; // Composition

    // Constructor injection
    public AlbumServiceImpl(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    @Override
    public List<Album> getAllAlbums() {
        return albumRepository.getAllAlbumsWithArtist();
    }

    @Override
    public List<Album> searchAlbum(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("Search keyword cannot be empty.");
            return List.of();
        }
        return albumRepository.searchAlbum(keyword.trim());
    }

    @Override
    public boolean addAlbum(Album album) {
        if (!isValid(album)) {
            return false;
        }
        return albumRepository.createAlbum(album);
    }

    @Override
    public boolean updateAlbum(Album album) {
        if (album.getId() <= 0) {
            System.out.println("Invalid album ID.");
            return false;
        }
        if (!isValid(album)) {
            return false;
        }
        return albumRepository.updateAlbum(album);
    }

    @Override
    public boolean deleteAlbum(int id) {
        if (id <= 0) {
            System.out.println("Invalid album ID.");
            return false;
        }
        return albumRepository.deleteAlbum(id);
    }

    // Simple validation rules before hitting the database
    private boolean isValid(Album album) {
        if (album.getName() == null || album.getName().trim().isEmpty()) {
            System.out.println("Album name is required.");
            return false;
        }
        int currentYear = Year.now().getValue();
        if (album.getYear() < 1900 || album.getYear() > currentYear + 1) {
            System.out.println("Please enter a valid year (1900-" + (currentYear + 1) + ").");
            return false;
        }
        if (album.getArtistId() <= 0) {
            System.out.println("A valid artist ID is required.");
            return false;
        }
        return true;
    }
}