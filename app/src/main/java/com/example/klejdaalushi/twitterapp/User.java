package com.example.klejdaalushi.twitterapp;


/**
 * Class that represents a user, which has several attributes
 */

public class User{
    private String name;
    private String screenName;
    private String id;
    private int friendsCount;
    private int followersCount;
    private String location;
    private String description;
    private String profileImageURL;
    private String bannerImageURL;

    /**
     * Constructor for the user class, which sets all the user attributes
     * @param name
     * @param screenName
     * @param id
     * @param friendsCount
     * @param followersCount
     * @param location
     * @param description
     * @param profileImageURL
     * @param bannerImageURL
     */
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

    public void friendsRemoved() {
        friendsCount --;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getName() {
        return name;
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
