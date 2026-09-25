package com.joysistvi.recordingapp.config;

import com.joysistvi.recordingapp.dao.ArtistDao;

public class Main {

    public static void main(String[] args) {
        DbConnection dbConnection = new DbConnection();
        ArtistDao artistDao = new ArtistDao(dbConnection);

    }
}
