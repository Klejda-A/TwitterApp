package com.example.klejdaalushi.twitterapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static TweetModel tweetModel = TweetModel.getInstance();
    private android.support.v4.app.ListFragment listFragment;
    private JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try{
            convert();
        }catch (JSONException jex){
            System.out.println(jex.getMessage());
        }
        tweetModel = new TweetModel(jsonArray);

        listFragment = new ListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_activity_main, listFragment).commit();



    }

    private String read() throws IOException{
        InputStream is = this.getResources().openRawResource(R.raw.test);
        byte[] b = new byte[is.available()];
        is.read(b);
        String output = new String(b);
        return output;
    }

    private void convert() throws JSONException{
        try {
            jsonArray = new JSONArray(read());
        } catch (IOException iex){
            System.out.println(iex.getMessage());
        }

    }
}
