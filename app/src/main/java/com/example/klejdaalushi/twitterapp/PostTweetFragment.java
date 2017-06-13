package com.example.klejdaalushi.twitterapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by Klejda Alushi on 13-Jun-17.
 */

public class PostTweetFragment extends android.support.v4.app.Fragment {
    private TweetModel tweetModel = TweetModel.getInstance();
    private EditText et_tweet_text;
    private Button btn_discard;
    private Button btn_post;
    private User user = tweetModel.getUsers().get(0);
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
                    new PostTweet(text).execute().get();
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

    private class PostTweet extends AsyncTask<String, Void, String> {
        private String tweetText;

        public PostTweet(String text){
            tweetText = text;
        }

        @Override
        protected String doInBackground(String... strings) {
            OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.twitter.com/1.1/statuses/update.json?status=" + tweetText, tweetModel.getAuthService());
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