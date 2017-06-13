package com.example.klejdaalushi.twitterapp;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.oauth.OAuth10aService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Klejda Alushi on 09-May-17.
 */

public class TweetModel {
    private static TweetModel tweetModel = null;
    private ArrayList<Tweet> tweets = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();
    private final OAuth10aService authService = new ServiceBuilder()
            .apiKey("tbsjqQneipEVvwpeCt4RrqVTk")
            .apiSecret("a8Nq26qFgt2QwcDdrjY0bwY1nL69DMwtc7NMvkFztMcJNlm6JM")
            .callback("https://www.saxion.nl/hbo-it/auth/twitter/callback")
            .build(TwitterApi.instance());
    private OAuth1AccessToken accessToken;
    private User currentUser;

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

    public void getTweets(String response) throws JSONException{
        JSONArray jsonArray = new JSONArray(response);
        createJSONobjects(jsonArray);
    }


    public OAuth10aService getAuthService() {
        return authService;
    }

    public void setAccessToken(OAuth1AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public OAuth1AccessToken getAccessToken() {
        return accessToken;
    }

    public void createJSONobjects(JSONArray jsonArray) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            tweetModel.getTweets().add(createTweet(object));
        }
    }

    public ArrayList<Tweet> createTweetsForUser(String response) throws JSONException {
        JSONArray jsonArray = new JSONArray(response);
        ArrayList<Tweet> userTweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            userTweets.add(createTweet(object));
        }
        return userTweets;
    }

    public Tweet createTweet(JSONObject object) throws JSONException{
        User creator = createUser(object.getJSONObject("user"));
        int userPosition = userExists(creator.getScreenName());
        if (userPosition > -1) {
            creator = users.get(userPosition);
        }
        else {
            tweetModel.getUsers().add(creator);

        }
        String createdAt = object.getString("created_at".toString());
        String text = object.getString("text".toString());
        String id = object.getString("id_str".toString());
        Tweet tweet = new Tweet(creator, createdAt, text, id);
        creator.addTweetToTweets(tweet);
        return tweet;
    }

    public User createUser(JSONObject object) throws JSONException {
        String profileBannerURL = "http://www.drodd.com/images14/blue22.jpg";
        if (object.has("profile_banner_url".toString())) {
            profileBannerURL = object.getString("profile_banner_url".toString());
        }
        User user = new User(object.getString("name".toString()), object.getString("screen_name".toString()),
                object.getString("id_str".toString()), object.getInt("friends_count"), object.getInt("followers_count"),
                object.getString("location".toString()), object.getString("description".toString()),
                object.getString("profile_image_url".toString()), profileBannerURL);
        return user;
    }

    public int userExists(String screenName) {
        int positionOfUser = -1;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getScreenName().equals(screenName)) {
                positionOfUser = i;
                break;
            }
        }
        return positionOfUser;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void createCurrentUser(String responseString) {
        try {
            JSONObject userObject = new JSONObject(responseString);
            currentUser = createUser(userObject);
            users.add(currentUser);
        }
        catch (JSONException jex) {
            jex.getMessage();
        }
    }
}
