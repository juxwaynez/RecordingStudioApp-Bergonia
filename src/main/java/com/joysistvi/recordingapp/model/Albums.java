package com.joysistvi.recordingapp.model;

public class Albums {

    private int id;
    private String title;
    private int releaseYear;
    private int artistId;

    // CONSTRUCTOR WITHOUT ID
    public Albums(String title, int releaseYear, int artistId) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.artistId = artistId;
    }

    // CONSTRUCTOR WITH ID
    public Albums (String title, int releaseYear) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.artistId = artistId;
    }

    // GETTERS AND SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", releaseYear=" + releaseYear +
                ", artistId=" + artistId +
                '}';
    }
}
