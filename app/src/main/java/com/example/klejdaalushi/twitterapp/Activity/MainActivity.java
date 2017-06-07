package com.example.klejdaalushi.twitterapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.klejdaalushi.twitterapp.CallbackInterface;
import com.example.klejdaalushi.twitterapp.ListFragment;
import com.example.klejdaalushi.twitterapp.R;
import com.example.klejdaalushi.twitterapp.TweetModel;
import com.example.klejdaalushi.twitterapp.WebViewFragment;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth10aService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends AppCompatActivity implements CallbackInterface{
    private static TweetModel tweetModel = TweetModel.getInstance();
    private ListFragment listFragment;
    private JSONArray jsonArray;
    private WebViewFragment webViewFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            convertJSON();
            tweetModel.createJSONobjects(jsonArray);
        }
        catch (IOException iex) {
            System.out.println(iex.getMessage());
        }
        catch (JSONException jex) {
            System.out.println(jex.getMessage());
        }

        webViewFragment = new WebViewFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_activity_main, webViewFragment).commit();

    }

    private String readJSONfile() throws IOException{
        InputStream is = getApplicationContext().getResources().openRawResource(R.raw.test);
        byte[] b = new byte[is.available()];
        is.read(b);
        String output = new String(b);
        return output;
    }

    private void convertJSON() throws JSONException, IOException{
        JSONObject jsonObject = new JSONObject(readJSONfile());
        jsonArray = jsonObject.getJSONArray("statuses");

    }

    @Override
    public void ListItemClicked(int position) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("POSITION", position);
        startActivity(intent);
    }



}

