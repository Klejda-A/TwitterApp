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
    private int id;
    private String location;
    private String description;
    private Tweet[] tweets;

    public User(String name, String screenName) {
        this.name = name;
        this.screenName = screenName;
    }

    public User(JSONObject object) throws JSONException{
        name = object.getString("name");
        screenName = object.getString("screen_name");
        id = object.getInt("id");
        location = object.getString("location");
        description = object.getString("description");
        tweets = new Tweet[object.getJSONArray("statuses").length()];
        for (int i = 0; i <tweets.length ; i++) {
            tweets[i] = new Tweet(object.getJSONArray("statuses").getJSONObject(i));
        }
    }
}
