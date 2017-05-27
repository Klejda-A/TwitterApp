package com.example.klejdaalushi.twitterapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends AppCompatActivity {
    private static TweetModel tweetModel = TweetModel.getInstance();
    private ListFragment listFragment;
    private JSONArray jsonArray;

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

        listFragment = new ListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_activity_main, listFragment).commit();



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

}
