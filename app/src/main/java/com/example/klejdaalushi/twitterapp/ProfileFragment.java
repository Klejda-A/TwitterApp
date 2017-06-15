package com.example.klejdaalushi.twitterapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Klejda Alushi on 13-Jun-17.
 */

public class ProfileFragment extends Fragment{
    private static TweetModel tweetModel = TweetModel.getInstance();
    private ImageView iv_profile_image;
    private ImageView iv_banner_image;
    private TextView tv_about_user;
    private TextView tv_friends_count;
    private TextView tv_followers_count;
    private Button btn_post_tweet;
    private User user;
    private boolean currentUserProfile = false;
    private ListFragment listFragment;
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

        int position = getArguments().getInt("Position");
        user = tweetModel.getUsers().get(position);
        if (user.getScreenName().equals(tweetModel.getCurrentUser().getScreenName())) {
            currentUserProfile = true;
        }

        iv_banner_image = (ImageView) rootView.findViewById(R.id.iv_profile_banner_image);
        iv_profile_image = (ImageView) rootView.findViewById(R.id.iv_profile_profile_image);
        tv_about_user = (TextView) rootView.findViewById(R.id.tv_profile_about_user);
        tv_followers_count = (TextView) rootView.findViewById(R.id.tv_profile_followers_count);
        tv_friends_count = (TextView) rootView.findViewById(R.id.tv_profile_friends_count);
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
        tv_friends_count.setText(user.getFriendsCount() + " FOLLOWING");
        tv_followers_count.setText(user.getFollowersCount() + " FOLLOWERS");

        btn_post_tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUserProfile == true) {
                    listener.postButtonClicked();
                }
            }
        });

        listFragment = new ListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("user", user.getScreenName());
        listFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.fl_profile_tweets, listFragment).commit();

        return rootView;
    }

    public void refresh() {
        listFragment.refresh();
    }

}
