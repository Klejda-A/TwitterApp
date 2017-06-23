package com.example.klejdaalushi.twitterapp.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.klejdaalushi.twitterapp.Fragment.UserListFragment;
import com.example.klejdaalushi.twitterapp.Interface.CallbackInterface;
import com.example.klejdaalushi.twitterapp.Fragment.PostTweetFragment;
import com.example.klejdaalushi.twitterapp.Fragment.ProfileFragment;
import com.example.klejdaalushi.twitterapp.Interface.ProfileInterface;
import com.example.klejdaalushi.twitterapp.R;

public class ProfileActivity extends AppCompatActivity implements CallbackInterface, ProfileInterface {
    private PostTweetFragment postTweetFragment;
    private ProfileFragment profileFragment;
    private UserListFragment userListFragment = new UserListFragment();
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
    }

    private void init() {
        position = getIntent().getExtras().getInt("Position");

        createProfileFragment();
    }

    private void createProfileFragment() {
        profileFragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("Position", position);
        profileFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_profile_activity, profileFragment).commit();
    }

    @Override
    public void ListItemClicked(int i) {

    }

    @Override
    public void onRefresh() {
        profileFragment.refresh();
    }

    @Override
    public void postButtonClicked() {
        postTweetFragment = new PostTweetFragment();
        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fl_profile_activity, postTweetFragment).commit();
    }

    @Override
    public void followersButtonClicked() {
        userListFragment.friendsList = false;
        addUserListFragment();
    }

    @Override
    public void friendsButtonClicked() {
        userListFragment.friendsList = true;
        addUserListFragment();
    }

    private void addUserListFragment() {
        Bundle bundle = new Bundle();
        bundle.putInt("Position", position);
        userListFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fl_profile_activity, userListFragment).commit();
    }


}
