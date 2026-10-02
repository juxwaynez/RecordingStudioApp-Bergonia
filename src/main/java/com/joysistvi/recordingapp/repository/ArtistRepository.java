package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.model.Artist;

import java.util.List;

public interface ArtistRepository {

    List<Artist> getAllArtists();

    List<Artist> searchArtist(String keyword);

    Artist getArtistById(int id);

    boolean createArtist(Artist artist);

    boolean updateArtist(Artist artist);

    boolean deleteArtist(int id);

    boolean archiveArtist(int id);

    boolean restoreArtist(int id);

    List<Artist> readArchivedArtist();
}
