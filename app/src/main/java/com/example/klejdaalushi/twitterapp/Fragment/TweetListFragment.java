package com.example.klejdaalushi.twitterapp.Fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.klejdaalushi.twitterapp.Interface.CallbackInterface;
import com.example.klejdaalushi.twitterapp.R;
import com.example.klejdaalushi.twitterapp.Tweet;
import com.example.klejdaalushi.twitterapp.TweetListAdapter;
import com.example.klejdaalushi.twitterapp.TweetModel;
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

public class TweetListFragment extends Fragment {
    private TweetModel tweetModel = TweetModel.getInstance();
    private TweetListAdapter tweetListAdapter;
    private ListView lv_tweets;
    private CallbackInterface listener;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Tweet> tweets;
    private String tweetToBeDeleted = "";

    public TweetListFragment() {

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
        } else if (getArguments().getString("search") != null) {
            try {
                String response = new SearchTweets(getArguments().getString("search")).execute().get();
                tweets = tweetModel.createTweets("object " + response);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException jex) {
                jex.getMessage();
            }
        } else {
            String userScreenName = getArguments().getString("user");
            try {
                String response = new getTweetsFromUser(userScreenName).execute().get();
                tweets = tweetModel.createTweets(response);
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
                listener.ListItemClicked(tweetModel.userExists(tweets.get(i).getCreator().getScreenName()));
            }
        });

        lv_tweets.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (tweets.get(i).getCreator().getScreenName().equals(tweetModel.getCurrentUser().getScreenName())) {
                    tweetToBeDeleted = tweets.get(i).getId();
                    createDeleteTweetAlertDialog();
                }
                return false;
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

    private void deleteTweet() {
        try {
            new DeleteTweet(tweetToBeDeleted).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        refresh();
    }

    private void createDeleteTweetAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to delete this tweet?");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteTweet();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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

    public class DeleteTweet extends AsyncTask<Boolean, Void, Boolean> {
        private String tweetID;

        public DeleteTweet(String id) {
            tweetID = id;
        }

        @Override
        protected Boolean doInBackground(Boolean... booleen) {
            OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.twitter.com/1.1/statuses/destroy/" + tweetID + ".json", tweetModel.getAuthService());
            tweetModel.getAuthService().signRequest(tweetModel.getAccessToken(), request);
            Response response = request.send();
            if (response.isSuccessful()) {
                return true;
            }
            return null;
        }
    }

    public class SearchTweets extends AsyncTask<String, Void, String> {
        private String searchText;

        public SearchTweets(String searchText) {
            this.searchText = searchText;
        }

        @Override
        protected String doInBackground(String... strings) {
            OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/search/tweets.json?q=" + searchText, tweetModel.getAuthService());
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
