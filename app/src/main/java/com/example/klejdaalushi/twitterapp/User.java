package com.example.klejdaalushi.twitterapp;

import java.util.ArrayList;

/**
 * Created by Klejda Alushi on 09-May-17.
 */

public class User {
    private String name;
    private String screenName;
    private int id;
    private String location;
    private String description;
    private ArrayList<Tweet> tweets;

    public User(String name, String screenName) {
        this.name = name;
        this.screenName = screenName;
    }
}
