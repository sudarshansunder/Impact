package com.adityawalvekar.impact.impact;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by vvvro on 12/10/2016.
 */

public class Post {
    int pid;
    String userName, title, description;
    String imgUrl;
    String location;
    Boolean active;
    int type;
    public void Post(){

    }
    public void Post(int pid, String userName, String description){
        this.pid = pid;
        this.userName = userName;
        this.description = description;
        type = 1;
    }

    public void Post(int pid, String userName, String title, String description, String imgUrl, String location, Boolean active){
        this.pid = pid;
        this.userName = userName;
        this.title = title;
        this.description = description;
        this.imgUrl = imgUrl;
        this.location = location;
        this.active = active;
        type = 2;
    }
}
