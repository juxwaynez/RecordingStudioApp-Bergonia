package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.model.Playlist;

import java.util.List;

public interface PlaylistRepo {

    List<Playlist> getAllPlaylists();
    Playlist getPlaylistById(int id);
    List<Playlist> searchPlaylist(String keyword);
    boolean createPlaylist(String name);
    boolean updatePlaylist(String name, int id);
    boolean archivePlaylist(int id);
    boolean deletePlaylist(int id);
    List<Playlist> getAllArchivedPlaylists();


}