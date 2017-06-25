package com.example.klejdaalushi.twitterapp.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.klejdaalushi.twitterapp.R;
import com.example.klejdaalushi.twitterapp.Tweet;
import com.example.klejdaalushi.twitterapp.TweetModel;
import java.util.ArrayList;

/**
 * Fragment class which enables user to search for tweets
 * Inflates the search_fragment layout
 */

public class SearchFragment extends Fragment {
    private EditText et_search_bar;
    private Button btn_search;
    private String searchedText;
    private TweetListFragment listFragment;
    private static final String SEARCH_BUTTON = "SEARCH_BUTTON";

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

    /**
     * Creates the list fragment to be shown in frame layout
     * Passes the text to be searched in a bundle
     */
    private void createListFragment() {
        listFragment = new TweetListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SEARCH_BUTTON, searchedText);
        listFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.fl_search, listFragment).commit();
    }

}
