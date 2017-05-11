package com.example.klejdaalushi.twitterapp;

import java.util.ArrayList;

/**
 * Created by Klejda Alushi on 09-May-17.
 */

public class Tweet {
    private User creator;
    private String createdAt;
    private String text;
    private String id;
    private boolean truncated;
    private ArrayList<Entity> entities;

    public Tweet(User creator, String createdAt, String text) {
        this.creator = creator;
        this.createdAt = createdAt;
        this.text = text;
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
}
