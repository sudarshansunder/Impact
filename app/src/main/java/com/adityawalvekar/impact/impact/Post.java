package com.adityawalvekar.impact.impact;

public class Post {

    int pid;
    String userName, title, description;
    String location;
    String dateTime;
    boolean active;
    int type;

    public Post() {

    }

    public Post(int pid, String userName, String description) {
        this.pid = pid;
        this.userName = userName;
        this.description = description;
        type = 1;
    }

    public Post(int pid, String userName, String title, String description, String location, String dateTime, boolean active) {
        this.pid = pid;
        this.userName = userName;
        this.title = title;
        this.description = description;
        this.location = location;
        this.active = active;
        this.dateTime = dateTime;
        type = 2;
    }
}