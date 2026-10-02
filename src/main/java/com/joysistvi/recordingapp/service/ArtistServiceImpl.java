package com.joysistvi.recordingapp.service;

import com.joysistvi.recordingapp.model.Artist;
import com.joysistvi.recordingapp.repository.ArtistRepository;

import java.util.List;

public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository; // Composition

    // Constructor injection
    public ArtistServiceImpl(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public List<Artist> getAllArtists() {
        return artistRepository.getAllArtists();
    }

    @Override
    public List<Artist> searchArtist(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("Search keyword cannot be empty.");
            return List.of();
        }
        return artistRepository.searchArtist(keyword.trim());
    }

    @Override
    public Artist getArtistById(int id) {
        if (id <= 0) {

            return null;
        }
        Artist artist = artistRepository.getArtistById(id);
        if (artist == null) {
            System.out.println("Artist not found.");
        }
        return artist;
    }

    @Override
    public boolean addArtist(Artist artist) {
        if (!isValid(artist)) {
            return false;
        }
        return artistRepository.createArtist(artist);
    }

    @Override
    public boolean updateArtist(Artist artist) {
        if (artist.getId() <= 0) {
            System.out.println("Invalid artist ID.");
            return false;
        }
        if (!isValid(artist)) {
            return false;
        }
        return artistRepository.updateArtist(artist);
    }

    @Override
    public boolean deleteArtist(int id) {
        if (id <= 0) {
            System.out.println("Invalid artist ID.");
            return false;
        }
        return artistRepository.deleteArtist(id);
    }

    @Override
    public boolean archiveArtist(int id) {
        if (id <= 0) {
            System.out.println("Invalid artist ID.");
            return false;
        }
        return artistRepository.archiveArtist(id);
    }

    @Override
    public boolean restoreArtist(int id) {
        if (id <= 0) {
            System.out.println("Invalid artist ID.");
            return false;
        }
        return artistRepository.restoreArtist(id);
    }

    @Override
    public List<Artist> getArchivedArtists() {
        return artistRepository.readArchivedArtist();
    }

    // Simple validation rules before hitting the database
    private boolean isValid(Artist artist) {
        if (artist.getName() == null || artist.getName().trim().isEmpty()) {
            System.out.println("Artist name is required.");
            return false;
        }
        return true;
    }
}