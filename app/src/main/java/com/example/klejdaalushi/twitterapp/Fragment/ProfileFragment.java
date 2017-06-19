package com.example.klejdaalushi.twitterapp.Fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.klejdaalushi.twitterapp.Interface.ProfileInterface;
import com.example.klejdaalushi.twitterapp.R;
import com.example.klejdaalushi.twitterapp.TweetModel;
import com.example.klejdaalushi.twitterapp.User;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutionException;

/**
 * Created by Klejda Alushi on 13-Jun-17.
 */

public class ProfileFragment extends Fragment{
    private static TweetModel tweetModel = TweetModel.getInstance();
    private ImageView iv_profile_image;
    private ImageView iv_banner_image;
    private TextView tv_about_user;
    private Button btn_friends_count;
    private Button btn_followers_count;
    private Button btn_post_tweet;
    private User user;
    private boolean currentUserProfile = false;
    private TweetListFragment tweetListFragment;
    private ProfileInterface listener;

    public ProfileFragment() {
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof ProfileInterface) {
            listener = (ProfileInterface) activity;
        }
        else {
            throw new ClassCastException();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.profile_fragment, container, false);

        final int position = getArguments().getInt("Position");
        user = tweetModel.getUsers().get(position);
        if (user.getScreenName().equals(tweetModel.getCurrentUser().getScreenName())) {
            currentUserProfile = true;
        }

        iv_banner_image = (ImageView) rootView.findViewById(R.id.iv_profile_banner_image);
        iv_profile_image = (ImageView) rootView.findViewById(R.id.iv_profile_profile_image);
        tv_about_user = (TextView) rootView.findViewById(R.id.tv_profile_about_user);
        btn_followers_count = (Button) rootView.findViewById(R.id.tv_profile_followers_count);
        btn_friends_count = (Button) rootView.findViewById(R.id.tv_profile_friends_count);
        btn_post_tweet = (Button) rootView.findViewById(R.id.btn_profile_post);
        if (currentUserProfile == false) {
            btn_post_tweet.setVisibility(View.GONE);
        }

        Picasso.with(getActivity().getApplicationContext()).load(user.getProfileImageURL()).into(iv_profile_image);
        Picasso.with(getActivity().getApplicationContext()).load(user.getBannerImageURL()).into(iv_banner_image);
        tv_about_user.setText(user.getName() + "\n" +
                user.getScreenName() + "\n" +
                user.getDescription() + "\n" +
                user.getLocation());
        btn_friends_count.setText(user.getFriendsCount() + " FOLLOWING");
        btn_followers_count.setText(user.getFollowersCount() + " FOLLOWERS");

        btn_post_tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUserProfile == true) {
                    listener.postButtonClicked();
                }
            }
        });
        btn_friends_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.friendsButtonClicked();
            }
        });
        btn_followers_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.followersButtonClicked();
            }
        });

        tweetListFragment = new TweetListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("user", user.getScreenName());
        tweetListFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.fl_profile_tweets, tweetListFragment).commit();

        return rootView;
    }

    public void refresh() {
        tweetListFragment.refresh();
    }

}
