package com.adityawalvekar.impact.impact;

public class Friends {
    String fullname;
    String username;
    boolean following;
    String image;

    public Friends() {

    }

    public Friends(String fullname, String username, boolean following) {
        this.fullname = fullname;
        this.username = username;
        this.following = following;
    }

    public Friends(String fullname, String username, String image, boolean following) {
        this.fullname = fullname;
        this.username = username;
        this.image = image;
        this.following = following;
    }
}
