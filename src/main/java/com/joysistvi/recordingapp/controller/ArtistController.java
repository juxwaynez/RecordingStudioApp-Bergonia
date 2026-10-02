package com.joysistvi.recordingapp.controller;

import com.joysistvi.recordingapp.model.Artist;
import com.joysistvi.recordingapp.service.ArtistService;

import java.util.List;

public class ArtistController {

    private final ArtistService artistService; // Composition

    // Constructor injection
    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    public List<Artist> handleViewAllArtists() {
        return artistService.getAllArtists();
    }

    public List<Artist> handleSearchArtist(String keyword) {
        return artistService.searchArtist(keyword);
    }

    public Artist handleGetArtistById(int id) {
        return artistService.getArtistById(id);
    }

    public boolean handleAddArtist(Artist artist) {
        return artistService.addArtist(artist);
    }

    public boolean handleUpdateArtist(Artist artist) {
        return artistService.updateArtist(artist);
    }

    public boolean handleDeleteArtist(int id) {
        return artistService.deleteArtist(id);
    }
}