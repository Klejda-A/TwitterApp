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
    private static final String USER_POSITION = "USER_POSITION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
    }

    /**
     * Method which gets the position of the user from the intent
     * And starts the profile fragment
     */
    private void init() {
        position = getIntent().getExtras().getInt(USER_POSITION);

        createProfileFragment();
    }

    private void createProfileFragment() {
        profileFragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(USER_POSITION, position);
        profileFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_profile_activity, profileFragment).commit();
    }

    @Override
    public void ListItemClicked(int position) {

    }

    /**
     * Method which refreses the whole profile fragment
     */
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

    /**
     * Creates a user list fragment, and passes the position of the user
     */
    private void addUserListFragment() {
        Bundle bundle = new Bundle();
        bundle.putInt(USER_POSITION, position);
        userListFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fl_profile_activity, userListFragment).commit();
    }


}
