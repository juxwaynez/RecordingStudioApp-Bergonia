package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.model.Albums;

import java.util.List;

public interface AlbumsRepo {

    List<Albums> getAllAlbums();
    Albums  readAlbumById(int id);
    List<Albums> searchAlbum(String keyword);
    boolean createAlbums (String title, int artistId);
    boolean updateAlbum (String title, int artistId, int id);
    boolean archiveAlbum(int id);
    boolean restoreAlbum(int id);
    boolean deleteAlbum(int id);
    List<Albums> readAllArchivedAlbums();
}