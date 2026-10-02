package com.joysistvi.recordingapp.controller;

import com.joysistvi.recordingapp.model.Album;
import com.joysistvi.recordingapp.service.AlbumService;

import java.util.List;

public class AlbumController {

    private final AlbumService albumService; // Composition

    // Constructor injection
    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    public List<Album> handleViewAllAlbums() {
        return albumService.getAllAlbums();
    }

    public List<Album> handleSearchAlbum(String keyword) {
        return albumService.searchAlbum(keyword);
    }

    public boolean handleAddAlbum(Album album) {
        return albumService.addAlbum(album);
    }

    public boolean handleUpdateAlbum(Album album) {
        return albumService.updateAlbum(album);
    }

    public boolean handleDeleteAlbum(int id) {
        return albumService.deleteAlbum(id);
    }
}