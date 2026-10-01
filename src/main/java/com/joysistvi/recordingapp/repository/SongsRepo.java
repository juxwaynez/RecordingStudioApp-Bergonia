package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.model.Songs;

import java.util.List;

public interface SongsRepo {

    List<Songs> getAllSongs();
    Songs getSongById(int id);
    List<Songs> searchSong(String keyword);
    boolean createSong(String title, int artistId);
    boolean updateSong(int id, String title, int artistId);
    boolean archiveSong(int id);
    boolean restoreSong(int id);
    boolean deleteSong(int id);
    List<Songs> getAllArchivedSongs();

}
