package com.example.klejdaalushi.twitterapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by Klejda Alushi on 09-May-17.
 */

public class ListFragment extends Fragment {
    private static TweetModel tweetModel = TweetModel.getInstance();
    private TweetListAdapter tweetListAdapter;
    private ListView lv_tweets;
    private CallbackInterface listener;

    public ListFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof CallbackInterface) {
            listener = (CallbackInterface) activity;
        }
        else {
            throw new ClassCastException();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_fragment, container, false);

        tweetListAdapter = new TweetListAdapter(getActivity(), R.layout.tweet_item, tweetModel.getTweets());
        lv_tweets = (ListView) rootView.findViewById(R.id.lv_tweets);
        lv_tweets.setAdapter(tweetListAdapter);

        lv_tweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listener.ListItemClicked(i);
            }
        });

        return rootView;
    }
}
