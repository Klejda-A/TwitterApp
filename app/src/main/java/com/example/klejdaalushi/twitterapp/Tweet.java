package com.example.klejdaalushi.twitterapp;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/**
 * Created by Klejda Alushi on 09-May-17.
 */

public class Tweet implements Parcelable{
    private User creator;
    private String createdAt;
    private String text;
    private String id;
    private boolean truncated;
    private ArrayList<Entity> entities;

    public Tweet(User creator, String createdAt, String text, String id) {
        this.creator = creator;
        this.createdAt = createdAt;
        this.text = text;
        this.id = id;
    }

    protected Tweet(Parcel in) {
        createdAt = in.readString();
        text = in.readString();
        id = in.readString();
        truncated = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(createdAt);
        dest.writeString(text);
        dest.writeString(id);
        dest.writeByte((byte) (truncated ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Tweet> CREATOR = new Creator<Tweet>() {
        @Override
        public Tweet createFromParcel(Parcel in) {
            return new Tweet(in);
        }

        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };

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
