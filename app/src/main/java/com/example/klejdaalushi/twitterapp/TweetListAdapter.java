package com.example.klejdaalushi.twitterapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Klejda Alushi on 09-May-17.
 */

public class TweetListAdapter extends ArrayAdapter<Tweet>{
    private static TweetModel tweetModel = TweetModel.getInstance();
    private TextView tv_creator;
    private TextView tv_text;
    private TextView tv_createdAt;


    public TweetListAdapter(Context context, int resource, ArrayList<Tweet> objects) {
        super(context, resource, objects);
        objects = tweetModel.getTweets();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tweet_item, parent, false);
        }

        final Tweet status = tweetModel.getTweets().get(position);
        tv_creator = (TextView) convertView.findViewById(R.id.tv_creator);
        tv_text = (TextView) convertView.findViewById(R.id.tv_text);
        tv_createdAt = (TextView) convertView.findViewById(R.id.tv_createdAt);
        tv_creator.setText(status.getCreator() + "");
        tv_text.setText(status.getText() + "");
        tv_createdAt.setText(status.getCreatedAt() + "");


        return convertView;
    }
}
