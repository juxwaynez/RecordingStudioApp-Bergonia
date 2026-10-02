package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.model.Album;

import java.util.List;

public interface AlbumRepository {

    List<Album> getAllAlbumsWithArtist();

    List<Album> searchAlbum(String keyword);

    boolean createAlbum(Album album);

    boolean updateAlbum(Album album);

    boolean deleteAlbum(int id);
}