package com.joysistvi.recordingapp.service;

import com.joysistvi.recordingapp.model.Album;

import java.util.List;

public interface AlbumService {

    List<Album> getAllAlbums();

    List<Album> searchAlbum(String keyword);

    boolean addAlbum(Album album);

    boolean updateAlbum(Album album);

    boolean deleteAlbum(int id);
}