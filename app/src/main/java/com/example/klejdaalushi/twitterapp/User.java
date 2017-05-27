package com.example.klejdaalushi.twitterapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Klejda Alushi on 09-May-17.
 */

public class User {
    private String name;
    private String screenName;
    private String id;
    private int friendsCount;
    private String location;
    private String description;
    private ArrayList<Tweet> tweets = new ArrayList<>();

    public User(String name, String screenName) {
        this.name = name;
        this.screenName = screenName;
    }



    public User(String name, String screenName, String id, int friendsCount, String location, String description) {
        this.name = name;
        this.screenName = screenName;
        this.id = id;
        this.friendsCount = friendsCount;
        this.location = location;
        this.description = description;
    }

    public String getScreenName() {
        return screenName;
    }

    public void addTweetToTweets(Tweet tweet) {
        tweets.add(tweet);
    }
}
