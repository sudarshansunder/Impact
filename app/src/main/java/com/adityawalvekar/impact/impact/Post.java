package com.adityawalvekar.impact.impact;

public class Post {

    int pid;
    String userName, title, description;
    String location;
    String dateTime;
    String userImage;
    String eventImage;
    boolean active;
    boolean attending;
    int type;
    private String date, time;

    public Post(int pid, String userName, String description, String dateTime, boolean active, String userImage) { //Case 1
        this.pid = pid;
        this.userName = userName;
        this.userImage = userImage;
        this.description = description;
        this.dateTime = dateTime;
        this.active = active;
        type = 1;
        parseDateTime();
    }

    public Post(int pid, String userName, String title, String description, String location, String dateTime, boolean active, String userImage, String eventImage, Boolean attending) { //Case 2
        this.pid = pid;
        this.userImage = userImage;
        this.eventImage = eventImage;
        this.userName = userName;
        this.title = title;
        this.description = description;
        this.location = location;
        this.active = active;
        this.dateTime = dateTime;
        this.attending = attending;
        parseDateTime();
        type = 2;
    }

    public Post(int pid, String userName, String userImage, String eventImage) { //Type 3
        this.pid = pid;
        this.userName = userName;
        this.userImage = userImage;
        this.eventImage = eventImage;
        type = 3;
    }

    private void parseDateTime() {
        //Input : 2016-12-10 15:35:46
        String temp = this.dateTime.substring(0, 10);
        String arrs[] = temp.split("-");
        String month = null;
        switch (arrs[1]) {
            case "01":
                month = "Jan";
                break;
            case "02":
                month = "Feb";
                break;
            case "03":
                month = "Mar";
                break;
            case "04":
                month = "Apr";
                break;
            case "05":
                month = "May";
                break;
            case "06":
                month = "Jun";
                break;
            case "07":
                month = "Jul";
                break;
            case "08":
                month = "Aug";
                break;
            case "09":
                month = "Sep";
                break;
            case "10":
                month = "Oct";
                break;
            case "11":
                month = "Nov";
                break;
            case "12":
                month = "Dec";
                break;
        }
        int day = Integer.valueOf(arrs[2]);
        this.date = "" + month + " " + day;
        this.time = this.dateTime.substring(11, dateTime.length() - 3);
        this.dateTime = null;
        if (type == 1)
            this.dateTime = "" + this.date + " at " + this.time;
        else
            this.dateTime = "" + this.date;
    }


}