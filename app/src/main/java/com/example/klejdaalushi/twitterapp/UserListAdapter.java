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
 * Created by Klejda Alushi on 19-Jun-17.
 */

public class UserListAdapter extends ArrayAdapter<User>{
    private static TweetModel tweetModel = TweetModel.getInstance();
    private TextView tv_name;
    private TextView tv_screen_name;
    private TextView tv_description;
    private ImageView iv_profile_image;
    private ArrayList<User> users;


    public UserListAdapter(Context context, int resource, ArrayList<User> objects) {
        super(context, resource, objects);
        users = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_item, parent, false);
        }

        final User user = users.get(position);
        tv_name = (TextView) convertView.findViewById(R.id.tv_user_name);
        tv_screen_name = (TextView) convertView.findViewById(R.id.tv_user_screen_name);
        iv_profile_image = (ImageView) convertView.findViewById(R.id.iv_user_profile_image);
        tv_description = (TextView) convertView.findViewById(R.id.tv_user_decription);
        Picasso.with(getContext()).load(user.getProfileImageURL()).into(iv_profile_image);
        tv_name.setText(user.getName() + " ");
        tv_screen_name.setText("@" + user.getScreenName() + "");
       tv_description.setText(user.getDescription() + "");


        return convertView;
    }
}
