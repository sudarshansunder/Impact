package com.adityawalvekar.impact.impact;

/**
 * Created by vvvro on 12/10/2016.
 */

public class Friends {
    String fullname;
    String username;
    Boolean following;
    String image;
    public Friends(){

    }
    public Friends(String fullname, String username, Boolean following){
        this.fullname = fullname;
        this.username = username;
        this.following = following;
    }
    public Friends(String fullname, String username, String image, Boolean following){
        this.fullname = fullname;
        this.username = username;
        this.image = image;
        this.following = following;
    }
}
