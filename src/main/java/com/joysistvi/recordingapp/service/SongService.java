package com.joysistvi.recordingapp.service;

import com.joysistvi.recordingapp.model.Song;

import java.util.List;

public interface SongService {

    List<Song> getAllSongs();

    Song getSongById(int id);

    List<Song> searchSong(String keyword);

    List<Song> getArchivedSongs();

    boolean addSong(Song song);

    boolean updateSong(Song song);

    boolean deleteSong(int id);

    boolean archiveSong(int id);

    boolean restoreSong(int id);
}