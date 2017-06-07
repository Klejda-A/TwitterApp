package com.example.klejdaalushi.twitterapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Klejda Alushi on 09-May-17.
 */

public class User {
    private String name;
    private String screenName;
    private String id;
    private int friendsCount;
    private int followersCount;
    private String location;
    private String description;
    private String profileImageURL;
    private String bannerImageURL;
    public ArrayList<Tweet> tweets = new ArrayList<>();

    public User(String name, String screenName, String id, int friendsCount,
                int followersCount, String location, String description,
                String profileImageURL, String bannerImageURL) {
        this.name = name;
        this.screenName = screenName;
        this.id = id;
        this.friendsCount = friendsCount;
        this.followersCount = followersCount;
        this.location = location;
        this.description = description;
        this.profileImageURL = profileImageURL;
        this.bannerImageURL = bannerImageURL;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getName() {
        return name;
    }

    public void addTweetToTweets(Tweet tweet) {
        tweets.add(tweet);
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public String getBannerImageURL() {
        return bannerImageURL;
    }
}
