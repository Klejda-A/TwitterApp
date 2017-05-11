package com.example.klejdaalushi.twitterapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by Klejda Alushi on 09-May-17.
 */

public class ListFragment extends android.support.v4.app.ListFragment {
    private static TweetModel tweetModel = TweetModel.getInstance();
    private TweetListAdapter tweetListAdapter;
    private ListView lv_tweets;

    public ListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_fragment, container, false);

        tweetListAdapter = new TweetListAdapter(getActivity(), R.layout.tweet_item, tweetModel.getTweets());
        lv_tweets = (ListView) rootView.findViewById(R.id.lv_tweets);
        lv_tweets.setAdapter(tweetListAdapter);

        return rootView;
    }
}
