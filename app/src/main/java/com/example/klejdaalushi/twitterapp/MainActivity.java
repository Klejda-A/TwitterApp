package com.example.klejdaalushi.twitterapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private static TweetModel tweetModel = TweetModel.getInstance();
    private android.support.v4.app.ListFragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listFragment = new ListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_activity_main, listFragment).commit();

    }
}
