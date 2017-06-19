package com.example.klejdaalushi.twitterapp.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.klejdaalushi.twitterapp.Interface.CallbackInterface;
import com.example.klejdaalushi.twitterapp.Fragment.TweetListFragment;
import com.example.klejdaalushi.twitterapp.R;
import com.example.klejdaalushi.twitterapp.Fragment.SearchFragment;
import com.example.klejdaalushi.twitterapp.TweetModel;
import com.example.klejdaalushi.twitterapp.Fragment.WebViewFragment;
import com.example.klejdaalushi.twitterapp.Interface.WebViewInterface;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;

import org.json.JSONException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity implements CallbackInterface, WebViewInterface{
    private static TweetModel tweetModel = TweetModel.getInstance();
    private TweetListFragment listFragment;
    private WebViewFragment webViewFragment;
    private SearchFragment searchFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(true);

        webViewFragment = new WebViewFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_activity_main, webViewFragment).commit();

    }



    @Override
    public void ListItemClicked(int i) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("Position", i);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        getUserAndTimeline();
        listFragment.refresh();
    }

    //added
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case  R.id.item_main_menu_user_profile:{
                ListItemClicked(tweetModel.userExists(tweetModel.getCurrentUser().getScreenName()));
                break;
            }

            case R.id.item_main_menu_search:{
                searchFragment = new SearchFragment();
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fl_activity_main, searchFragment).commit();
                break;
            }

        }
        return true;
    }



    @Override
    public void userInfo(String responseString){
        tweetModel.createCurrentUser(responseString);
        getUserAndTimeline();

        listFragment = new TweetListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_activity_main, listFragment).commit();
    }

    private void getUserAndTimeline() {
        try {
            tweetModel.getTweets().clear();
            String timelineTweets = new getTimeline().execute().get();
            tweetModel.getTweets(timelineTweets);
        }
        catch (JSONException jex) {
            jex.getMessage();
        }
        catch (InterruptedException iex) {
            iex.getMessage();
        }
        catch (ExecutionException eex) {
            eex.getMessage();
        }
    }

    private class getTimeline extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/statuses/home_timeline.json", tweetModel.getAuthService());
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

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }


}

