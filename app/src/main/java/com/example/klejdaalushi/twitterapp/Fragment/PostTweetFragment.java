package com.example.klejdaalushi.twitterapp.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.klejdaalushi.twitterapp.R;
import com.example.klejdaalushi.twitterapp.TweetModel;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import java.util.concurrent.ExecutionException;

/**
 * Fragment with which a user can post a tweet
 * Inflates the post_tweet_fragment layout
 */

public class PostTweetFragment extends android.support.v4.app.Fragment {
    private TweetModel tweetModel = TweetModel.getInstance();
    private EditText et_tweet_text;
    private Button btn_discard;
    private Button btn_post;
    private String text;

    public PostTweetFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.post_tweet_fragment, container, false);

        et_tweet_text = (EditText) rootView.findViewById(R.id.et_post_tweet);
        btn_discard = (Button) rootView.findViewById(R.id.btn_discard_tweet);
        btn_post = (Button) rootView.findViewById(R.id.btn_post_tweet);

        btn_discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    text = et_tweet_text.getText().toString();
                    //checks if text is empty, since user cant post empty tweet
                    if (!text.equals("")) {
                        boolean tweetPosted = new PostTweet(text).execute().get();
                        if (tweetPosted) {
                            Toast.makeText(getContext(), "Tweet was posted", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getContext(), "Tweet cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                    getActivity().onBackPressed();
                }
                catch (InterruptedException iex) {
                    iex.getMessage();
                }
                catch (ExecutionException eex) {
                    eex.getMessage();
                }
            }
        });

        return rootView;
    }

    /**
     * AsyncTask which sends a post request to twitter server
     */
    private class PostTweet extends AsyncTask<Boolean, Void, Boolean> {
        private String tweetText;

        public PostTweet(String text) {
            tweetText = text;
        }

        /**
         * Creates, signs, and send the request
         * @param booleen
         * @return whether the response is successful or not
         */
        @Override
        protected Boolean doInBackground(Boolean... booleen) {
            OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.twitter.com/1.1/statuses/update.json?status=" + tweetText, tweetModel.getAuthService());
            tweetModel.getAuthService().signRequest(tweetModel.getAccessToken(), request);
            Response response = request.send();
            if (response.isSuccessful()) {
                return true;
            }
            return null;
        }
    }
}