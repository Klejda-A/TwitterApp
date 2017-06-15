package com.example.klejdaalushi.twitterapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by Klejda Alushi on 13-Jun-17.
 */

public class SearchFragment extends Fragment {
    private TweetModel tweetModel = TweetModel.getInstance();
    private EditText et_search_bar;
    private Button btn_search;
    private String searchedText;
    private ArrayList<Tweet> searchTweets = new ArrayList<>();
    private ListFragment listFragment;

    public SearchFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.search_fragment, container, false);

        et_search_bar = (EditText) rootView.findViewById(R.id.et_search_bar);
        btn_search = (Button) rootView.findViewById(R.id.btn_search);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchedText = et_search_bar.getText().toString();
                createListFragment();
            }
        });


        return rootView;
    }

    private void createListFragment() {
        listFragment = new ListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("search", searchedText);
        listFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.fl_search, listFragment).commit();
    }

}
