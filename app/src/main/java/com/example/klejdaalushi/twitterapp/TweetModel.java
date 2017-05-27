package com.example.klejdaalushi.twitterapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Klejda Alushi on 09-May-17.
 */

public class TweetModel {
    private static TweetModel tweetModel = null;
    private ArrayList<Tweet> tweets = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();

    private TweetModel() {
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


    public void createJSONobjects(JSONArray jsonArray) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            tweets.add(createTweet(object));
        }
    }

    public Tweet createTweet(JSONObject object) throws JSONException{
        User creator = createUser(object.getJSONObject("user"));
        users.add(creator);
        String createdAt = object.getString("created_at".toString());
        String text = object.getString("text".toString());
        String id = object.getString("id_str".toString());
        Tweet tweet = new Tweet(creator, createdAt, text, id);
        creator.addTweetToTweets(tweet);
        return tweet;
    }

    public User createUser(JSONObject object) throws JSONException {
        String name = object.getString("name".toString());
        String screenName = object.getString("screen_name".toString());
        String id = object.getString("id_str".toString());
        int friendsCount = object.getInt("friends_count");
        String location = object.getString("location".toString());
        String description = object.getString("description".toString());
        User user = new User(name, screenName, id, friendsCount, location, description);
        return user;
    }
}
