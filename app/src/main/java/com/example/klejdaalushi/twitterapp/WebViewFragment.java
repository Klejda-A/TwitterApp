package com.example.klejdaalushi.twitterapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by Klejda Alushi on 07-Jun-17.
 */

public class WebViewFragment extends Fragment {
    private TweetModel tweetModel = TweetModel.getInstance();
    private WebView wv_website;
    private static OAuth1RequestToken requestToken;
    private String verifier;
    private OAuth1AccessToken accessToken;


    public WebViewFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.webview_fragment, container, false);

        String authUrl = null;
        try {
            authUrl = new authUrl().execute().get();
        } catch (Exception e) {
            e.getMessage();
        }
        wv_website = (WebView) rootView.findViewById(R.id.wv_main_activity_page);

        wv_website.loadUrl(authUrl);
        wv_website.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("https://www.saxion.nl/hbo-it/auth/twitter/callback")) {
                    verifier = Uri.parse(url).getQueryParameter("oauth_verifier");
                    try {
                        accessToken = new getAccessToken().execute().get();
                        if (accessToken != null) {
                            tweetModel.setAccessToken(accessToken);
                            new sendRequest().execute().get();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });



        return rootView;
    }

    private class authUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                requestToken = tweetModel.getAuthService().getRequestToken();
                final String authorizationUrl = tweetModel.getAuthService().getAuthorizationUrl(requestToken);
                return authorizationUrl;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }


    private class getAccessToken extends AsyncTask<OAuth1AccessToken, Void, OAuth1AccessToken> {

        @Override
        protected OAuth1AccessToken doInBackground(OAuth1AccessToken... params) {
            try {
                accessToken = tweetModel.getAuthService().getAccessToken(requestToken, verifier);
                return accessToken;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private class sendRequest extends AsyncTask<Boolean, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Boolean... strings) {
            OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/account/verify_credentials.json", tweetModel.getAuthService());
            tweetModel.getAuthService().signRequest(accessToken, request);
            Response response = request.send();
            if (response.isSuccessful()) {
                try {
                    String responseString = response.getBody();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
            return false;
        }
    }

}

