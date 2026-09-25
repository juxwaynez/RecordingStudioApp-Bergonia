package com.joysistvi.recordingapp.dao;

import com.joysistvi.recordingapp.config.DbConnection;

public class ArtistDao {

    // COMPOSITION
    private final DbConnection dbConnection;

    // CONSTRUCTOR
    public ArtistDao(DbConnection dbConnection) {
            this.dbConnection = dbConnection;
    }

    // CRUD OPERATION
        public void readlAllArtist() {
            String query = "SELECT * FROM artist WHERE is archived = 0";

            











        }










}
