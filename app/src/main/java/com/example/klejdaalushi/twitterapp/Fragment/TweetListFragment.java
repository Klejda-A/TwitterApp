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
 * Fragment which creates a list of tweets
 */

public class TweetListFragment extends Fragment {
    private TweetModel tweetModel = TweetModel.getInstance();
    private TweetListAdapter tweetListAdapter;
    private ListView lv_tweets;
    private CallbackInterface listener;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Tweet> tweets;
    private static final String USER_SCREEN_NAME = "USER_SCREEN_NAME";
    private static final String SEARCH_BUTTON = "SEARCH_BUTTON";

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

        try {
            //checks the type of argument that is passed
            if (getArguments() == null) {
                //arguments are null when the main timeline tweets should be shown
                tweets = tweetModel.getTweets();
            } else if (getArguments().getString(SEARCH_BUTTON) != null) {
                //string argument is 'search' when the searched tweets should be shown
                String response = new SearchTweets(getArguments().getString("search")).execute().get();
                tweets = tweetModel.createTweets("object " + response);
            } else {
                //else string argument is 'user' when the tweets that a certain user has posted, need to be shown
                String userScreenName = getArguments().getString(USER_SCREEN_NAME);
                String response = new GetTweetsFromUser(userScreenName).execute().get();
                tweets = tweetModel.createTweets(response);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException jex) {
            jex.getMessage();
        }
        tweetListAdapter = new TweetListAdapter(getActivity(), R.layout.tweet_item, tweets);
        lv_tweets = (ListView) rootView.findViewById(R.id.lv_tweets);
        lv_tweets.setAdapter(tweetListAdapter);
        //is used to refresh the layout when a tweet is posted or deleted
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
                    createDeleteTweetAlertDialog(i);
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

    /**
     * Method which deletes a tweet, by calling the AsyncTask DeleteTweet
     * @param userPosition
     */
    private void deleteTweet(int userPosition) {
        try {
            new DeleteTweet(tweets.get(userPosition).getId()).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        tweets.remove(userPosition);
        refresh();
    }

    /**
     * Method which creates an alert dialog to verify deletion of tweet
     * @param userPosition
     */
    private void createDeleteTweetAlertDialog(final int userPosition) {
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
                deleteTweet(userPosition);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Method that refreshes the tweet adapter
     */
    public void refresh() {
        tweetListAdapter.notifyDataSetChanged();
    }

    /**
     * AsyncTask which gets all the tweets posted by a certain user
     */
    public class GetTweetsFromUser extends AsyncTask<String, Void, String> {
        private String url;

        /**
         * Constructor for the GetTweetsFromUser which creates the url to be used in request
         * @param screenName
         */
        public GetTweetsFromUser(String screenName) {
            url = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=" + screenName;
        }

        /**
         * Method which creates, signs, and send GET request to twitter server, to get tweets of user
         * @param params
         * @return body if response is successful
         * @return null if it is not
         */
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

    /**
     * AsyncTask which deletes a tweet posted by the current user
     */
    public class DeleteTweet extends AsyncTask<Boolean, Void, Boolean> {
        private String tweetID;

        /**
         * Constructor for the DeleteTweet class, which pases the if of the tweet to be deleted
         * @param id
         */
        public DeleteTweet(String id) {
            tweetID = id;
        }

        /**
         * Method which creates, signs, and send POST request to twitter server, to delete a tweet
         * @param booleen
         * @return whether the response is successful or not
         */
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

    /**
     * AsyncTask which gets tweets based on text that user searched
     */
    public class SearchTweets extends AsyncTask<String, Void, String> {
        private String searchText;

        /**
         * Constructor for the SearchTweets class, and passes the searched text
         * @param searchText
         */
        public SearchTweets(String searchText) {
            this.searchText = searchText;
        }

        /**
         * Method which creates, signs, and send GET request to twitter server for the searched tweets
         * @param strings
         * @return body if response is successful
         * @return null if it is not
         */
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
