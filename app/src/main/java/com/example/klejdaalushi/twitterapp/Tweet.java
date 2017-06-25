package com.example.klejdaalushi.twitterapp;

/**
 * Class that represents a tweet/status, which has a creator, a date on which it is created, text, an id, and
 * an image (if applicable)
 */

public class Tweet{
    private User creator;
    private String createdAt;
    private String text;
    private String id;
    private String imageUrl;

    /**
     * Constructor for the tweet class, which sets all the tweet attributes
     * @param creator
     * @param createdAt
     * @param text
     * @param id
     * @param imageUrl
     */
    public Tweet(User creator, String createdAt, String text, String id, String imageUrl) {
        this.creator = creator;
        this.createdAt = createdAt;
        this.text = text;
        this.id = id;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public User getCreator() {
        return creator;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getText() {
        return text;
    }

    public String getId() {
        return id;
    }
}
