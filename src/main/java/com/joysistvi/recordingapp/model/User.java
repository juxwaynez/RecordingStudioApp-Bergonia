package com.joysistvi.recordingapp.model;

public class User {

    private int id;
    private String username;
    private String email;

    // CONSTRUCTOR WITHOUT ID
    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    // CONSTRUCTOR WITH ID
    public User(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    // GETTERS AND SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
