package com.joysistvi.recordingapp.model;

public class Song {
    private int id;
    private String title;
    private String length;
    private String genre;
    private String albumName;
    private int albumId;

    public Song() {
    }

    public Song(int id, String title, String length, String genre, String albumName) {
        this.id = id;
        this.title = title;
        this.length = length;
        this.genre = genre;
        this.albumName = albumName;
    }

    public Song(String title, String length, String genre, int albumId) {
        this.title = title;
        this.length = length;
        this.genre = genre;
        this.albumId = albumId;
    }

    public Song(int id, String title, String length, String genre, int albumId) {
        this.id = id;
        this.title = title;
        this.length = length;
        this.genre = genre;
        this.albumId = albumId;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

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

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", length='" + length + '\'' +
                ", genre='" + genre + '\'' +
                ", albumName='" + albumName + '\'' +
                '}';
    }
}
