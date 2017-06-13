package com.example.klejdaalushi.twitterapp.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.klejdaalushi.twitterapp.CallbackInterface;
import com.example.klejdaalushi.twitterapp.PostTweetFragment;
import com.example.klejdaalushi.twitterapp.ProfileFragment;
import com.example.klejdaalushi.twitterapp.ProfileInterface;
import com.example.klejdaalushi.twitterapp.R;

public class ProfileActivity extends AppCompatActivity implements CallbackInterface, ProfileInterface {
    private PostTweetFragment postTweetFragment;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
    }

    private void init() {
        final int position = getIntent().getExtras().getInt("POSITION");

        profileFragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("Position", position);
        profileFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_profile_activity, profileFragment).commit();


    }

    @Override
    public void ListItemClicked(int position) {

    }

    @Override
    public void onRefresh() {
//        listFragment.refresh();
    }

    @Override
    public void postButtonClicked() {
        postTweetFragment = new PostTweetFragment();
        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fl_profile_activity, postTweetFragment).commit();
    }
}
