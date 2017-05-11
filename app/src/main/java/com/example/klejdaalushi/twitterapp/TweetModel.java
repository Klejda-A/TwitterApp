package com.example.klejdaalushi.twitterapp;

import java.util.ArrayList;

/**
 * Created by Klejda Alushi on 09-May-17.
 */

public class TweetModel {
    private static TweetModel tweetModel = null;
    private ArrayList<Tweet> tweets = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();

    private TweetModel () {
        users.add(new User("Klejda", "Klejda.A"));
        users.add(new User("John", "John.B"));
        tweets.add(new Tweet(users.get(0), "09.05.2017", "Hey"));
        tweets.add(new Tweet(users.get(0), "08.05.2017", "Hello"));
        tweets.add(new Tweet(users.get(0), "09.03.2017", "Goodbye"));
        tweets.add(new Tweet(users.get(1), "01.07.2017", "Go away"));
        tweets.add(new Tweet(users.get(1), "04.05.2017", "Bye"));

    }

    public static TweetModel getInstance() {
        if (tweetModel == null) {
            tweetModel = new TweetModel();
        }
        return tweetModel;
    }

    public ArrayList<Tweet> getTweets() {
        return tweets;
    }

    public ArrayList<User> getUsers() {
        return users;
    }
}