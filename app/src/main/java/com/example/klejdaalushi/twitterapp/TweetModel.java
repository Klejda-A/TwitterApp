package com.example.klejdaalushi.twitterapp;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.oauth.OAuth10aService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Singleton tweet model, which creates various twitter objects, and contains the current user who is using the app
 */

public class TweetModel {
    private static TweetModel tweetModel = null;
    private final OAuth10aService authService = new ServiceBuilder()
            .apiKey("tbsjqQneipEVvwpeCt4RrqVTk")
            .apiSecret("a8Nq26qFgt2QwcDdrjY0bwY1nL69DMwtc7NMvkFztMcJNlm6JM")
            .callback("https://www.saxion.nl/hbo-it/auth/twitter/callback")
            .build(TwitterApi.instance());
    private OAuth1AccessToken accessToken;
    private ArrayList<Tweet> tweets = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();
    private User currentUser;

    private TweetModel() {
    }


    public static TweetModel getInstance() {
        if (tweetModel == null) {
            tweetModel = new TweetModel();
        }
        return tweetModel;
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

    public ArrayList<Tweet> getTweets() {
        return tweets;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void getTweets(String response) throws JSONException {
        JSONArray jsonArray = new JSONArray(response);
        createJSONobjects(jsonArray);
    }

    /**
     * Method to create tweets that are shown in the main timeline
     * Returns void, since the tweets created, are stored in the users arraylist in the tweetModel
     * @param jsonArray
     * @throws JSONException
     */
    public void createJSONobjects(JSONArray jsonArray) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            tweetModel.getTweets().add(createTweet(object));
        }
    }

    /**
     * Method which creates a list of tweets either created by a certain user, or which show up after a search
     * @param response
     * @return tweetList
     * @throws JSONException
     */
    public ArrayList<Tweet> createTweets(String response) throws JSONException {
        JSONArray jsonArray;
        //checks where the reponse starts with 'object'
        if (response.startsWith("object")) {
            //if so replaces that with empty space, in order to create object
            response = response.replace("object ", "");
            JSONObject tweetObject = new JSONObject(response);
            jsonArray = tweetObject.getJSONArray("statuses");
        } else {
            jsonArray = new JSONArray(response);
        }

        ArrayList<Tweet> tweetList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            tweetList.add(createTweet(object));
        }
        return tweetList;
    }

    /**
     * Method to create a list of users, either followers of friends of a certain user
     * @param response
     * @return list of friends or followers
     * @throws JSONException
     */
    public ArrayList<User> createUsers(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray("users");
        ArrayList<User> followersOrFriends = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            followersOrFriends.add(createUser(object));
        }
        return followersOrFriends;
    }

    /**
     * Method to create a tweet, from a JSONObject
     * @param object
     * @return tweet
     * @throws JSONException
     */
    public Tweet createTweet(JSONObject object) throws JSONException {
        User creator = createUser(object.getJSONObject("user"));
        //checks if user exists, so there are no duplicates
        int userPosition = userExists(creator.getScreenName());
        if (userPosition > -1) {
            creator = users.get(userPosition);
        } else {
            tweetModel.getUsers().add(creator);

        }
        String createdAt = object.getString("created_at".toString());
        String text = object.getString("text".toString());
        String id = object.getString("id_str".toString());
        String url = "";
        //checks if tweet has an image,
        //if so it get the url of that image
        //if not the url remains empty
        if (object.has("extended_entities")) {
            JSONObject mediaURL = object.getJSONObject("extended_entities").getJSONArray("media").getJSONObject(0);
            url = mediaURL.getString("media_url".toString());
        }
        Tweet tweet = new Tweet(creator, createdAt, text, id, url);
        return tweet;
    }

    /**
     * Creates a user from a JSONObject
     * @param object
     * @return user
     * @throws JSONException
     */
    public User createUser(JSONObject object) throws JSONException {
        //default profile banner image
        String profileBannerURL = "http://www.drodd.com/images14/blue22.jpg";
        //if object has a banner image, then that replaces the default
        if (object.has("profile_banner_url".toString())) {
            profileBannerURL = object.getString("profile_banner_url".toString());
        }
        User user = new User(object.getString("name".toString()), object.getString("screen_name".toString()),
                object.getString("id_str".toString()), object.getInt("friends_count"), object.getInt("followers_count"),
                object.getString("location".toString()), object.getString("description".toString()),
                object.getString("profile_image_url".toString()), profileBannerURL);
        return user;
    }

    /**
     * Method to check if a certain user exists, and if so to find their position in the users list
     * @param screenName
     * @return position of user, if they exist
     * @return -1, if user does not exist
     */
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

    /**
     * Creates the current user from the response gotten from the VerifyCredentials Async class
     * @param responseString
     */
    public void createCurrentUser(String responseString) {
        try {
            JSONObject userObject = new JSONObject(responseString);
            currentUser = createUser(userObject);
            users.add(currentUser);
        } catch (JSONException jex) {
            jex.getMessage();
        }
    }
}
