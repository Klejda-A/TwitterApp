package com.example.klejdaalushi.twitterapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.klejdaalushi.twitterapp.Activity.MainActivity;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Klejda Alushi on 09-May-17.
 */

public class ListFragment extends Fragment {
    private static TweetModel tweetModel = TweetModel.getInstance();
    private TweetListAdapter tweetListAdapter;
    private ListView lv_tweets;
    private CallbackInterface listener;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Tweet> tweets;

    public ListFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof CallbackInterface) {
            listener = (CallbackInterface) activity;
        } else {
            throw new ClassCastException();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.list_fragment, container, false);

        if (getArguments() == null) {
            tweets = tweetModel.getTweets();
        }
        else {
            String userScreenName = getArguments().getString("user");
            try {
                String response = new getTweetsFromUser(userScreenName).execute().get();
                tweets = tweetModel.createTweetsForUser(response);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException jex) {
                jex.getMessage();
            }
        }
        tweetListAdapter = new TweetListAdapter(getActivity(), R.layout.tweet_item, tweets);
        lv_tweets = (ListView) rootView.findViewById(R.id.lv_tweets);
        lv_tweets.setAdapter(tweetListAdapter);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);

        lv_tweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listener.ListItemClicked(i);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listener.onRefresh();
                swipeRefreshLayout.setRefreshing(false);

            }
        });



        return rootView;
    }

    public void refresh() {
        tweetListAdapter.notifyDataSetChanged();
    }


    public class getTweetsFromUser extends AsyncTask<String, Void, String> {
        private String url;

        public getTweetsFromUser(String screenName) {
            url = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=" + screenName;
        }

        @Override
        protected String doInBackground(String... params) {
            OAuthRequest request = new OAuthRequest(Verb.GET, url, tweetModel.getAuthService());
            tweetModel.getAuthService().signRequest(tweetModel.getAccessToken(), request);
            Response response = request.send();
            if (response.isSuccessful()) {
                try {
                    return response.getBody();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
