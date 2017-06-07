package com.example.klejdaalushi.twitterapp.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.klejdaalushi.twitterapp.R;
import com.example.klejdaalushi.twitterapp.TweetModel;
import com.example.klejdaalushi.twitterapp.User;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    private static TweetModel tweetModel = TweetModel.getInstance();
    private ImageView iv_profile_image;
    private ImageView iv_banner_image;
    private TextView tv_about_user;
    private TextView tv_friends_count;
    private TextView tv_followers_count;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
    }

    private void init() {
        user = tweetModel.getUsers().get(getIntent().getExtras().getInt("POSITION"));
        iv_banner_image = (ImageView) findViewById(R.id.iv_profile_banner_image);
        iv_profile_image = (ImageView) findViewById(R.id.iv_profile_profile_image);
        tv_about_user = (TextView) findViewById(R.id.tv_profile_about_user);
        tv_followers_count = (TextView) findViewById(R.id.tv_profile_followers_count);
        tv_friends_count = (TextView) findViewById(R.id.tv_profile_friends_count);

        Picasso.with(getApplicationContext()).load(user.getProfileImageURL()).into(iv_profile_image);
        Picasso.with(getApplicationContext()).load(user.getBannerImageURL()).into(iv_banner_image);
        tv_about_user.setText(user.getName() + "\n" +
                user.getScreenName() + "\n" +
                user.getDescription() + "\n" +
                user.getLocation());
        tv_friends_count.setText(user.getFriendsCount() + " FOLLOWING");
        tv_followers_count.setText(user.getFollowersCount() + " FOLLOWERS");
    }
}
