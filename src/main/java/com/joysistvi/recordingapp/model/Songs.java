package com.joysistvi.recordingapp.model;

public class Songs {

    private int id;
    private String title;
    private int durationInSeconds;
    private int albumId; // Links this song to an Album by its ID

    // CONSTRUCTOR WITHOUT ID
    public Songs(String title, int durationInSeconds, int albumId) {
        this.title = title;
        this.durationInSeconds = durationInSeconds;
        this.albumId = albumId;
    }

    // CONSTRUCTOR WITH ID
    public Songs(int id, String title, int durationInSeconds, int albumId) {
        this.id = id;
        this.title = title;
        this.durationInSeconds = durationInSeconds;
        this.albumId = albumId;
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

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setDurationInSeconds(int durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", durationInSeconds=" + durationInSeconds +
                ", albumId=" + albumId +
                '}';
    }
}
