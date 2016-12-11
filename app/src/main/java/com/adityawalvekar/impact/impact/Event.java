package com.adityawalvekar.impact.impact;

import android.util.Log;

public class Event {

    String username, userImage, eventImage, date, location, title, desc;

    public Event(String username, String userImage, String eventImage, String date, String location, String title, String desc) {
        this.userImage = userImage;
        this.username = username;
        Log.d("User image", this.userImage);
        this.eventImage = eventImage;
        this.date = date;
        this.title = title;
        this.desc = desc;
        this.location = location;
    }

}
