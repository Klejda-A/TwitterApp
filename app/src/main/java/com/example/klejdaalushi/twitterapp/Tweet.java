package com.example.klejdaalushi.twitterapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Klejda Alushi on 09-May-17.
 */

public class Tweet{
    private User creator;
    private String createdAt;
    private String text;
    private String id;
    private boolean truncated;
    private String image_url;
    private ArrayList<Entity> entities;

    public Tweet(User creator, String createdAt, String text, String id) {
        this.creator = creator;
        this.createdAt = createdAt;
        this.text = text;
        this.id = id;
    }

    public Tweet(User creator, String createdAt, String text, String id, String image_url) {
        this.creator = creator;
        this.createdAt = createdAt;
        this.text = text;
        this.id = id;
        this.image_url = image_url;
    }

    public String getImage_url() {
        return image_url;
    }

    protected Tweet(Parcel in) {
        createdAt = in.readString();
        text = in.readString();
        id = in.readString();
        truncated = in.readByte() != 0;
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
