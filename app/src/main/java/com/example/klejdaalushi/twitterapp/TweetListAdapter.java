package com.example.klejdaalushi.twitterapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Klejda Alushi on 09-May-17.
 */

public class TweetListAdapter extends ArrayAdapter<Tweet>{
    private static TweetModel tweetModel = TweetModel.getInstance();
    private TextView tv_creator_name;
    private TextView tv_creator_screen_name;
    private TextView tv_text;
    private TextView tv_createdAt;
    private ImageView iv_profile_image;
    private ArrayList<Tweet> tweets;



    public TweetListAdapter(Context context, int resource, ArrayList<Tweet> objects) {
        super(context, resource, objects);
        tweets = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tweet_item, parent, false);
        }

        final Tweet tweet = tweets.get(position);
        tv_creator_name = (TextView) convertView.findViewById(R.id.tv_tweet_creator_name);
        tv_creator_screen_name = (TextView) convertView.findViewById(R.id.tv_tweet_creator_screen_name);
        tv_text = (TextView) convertView.findViewById(R.id.tv_tweet_text);
        tv_createdAt = (TextView) convertView.findViewById(R.id.tv_tweet_createdAt);
        iv_profile_image = (ImageView) convertView.findViewById(R.id.iv_tweet_profile_image);
        Picasso.with(getContext()).load(tweet.getCreator().getProfileImageURL()).into(iv_profile_image);
        tv_creator_name.setText(tweet.getCreator().getName() + " ");
        tv_creator_screen_name.setText("@" + tweet.getCreator().getScreenName() + "");
        tv_text.setText(tweet.getText() + "");
        tv_createdAt.setText(tweet.getCreatedAt() + "");


        return convertView;
    }


}
