package com.example.klejdaalushi.twitterapp.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.klejdaalushi.twitterapp.Interface.CallbackInterface;
import com.example.klejdaalushi.twitterapp.Interface.ProfileInterface;
import com.example.klejdaalushi.twitterapp.R;
import com.example.klejdaalushi.twitterapp.TweetModel;
import com.example.klejdaalushi.twitterapp.User;
import com.example.klejdaalushi.twitterapp.UserListAdapter;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Klejda Alushi on 19-Jun-17.
 */

public class UserListFragment extends Fragment {
    private static TweetModel tweetModel = TweetModel.getInstance();
    private UserListAdapter userListAdapter;
    private ListView lv_users;
    private User user;
    private ArrayList<User> users;
    public boolean friendsList = false;
    private String response;
    private CallbackInterface listener;

    public UserListFragment() {

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

        int position = getArguments().getInt("Position");
        user = tweetModel.getUsers().get(position);
        try {
            response = new GetUsers(user.getScreenName()).execute().get();
            users = tweetModel.createUsers(response);
        } catch (ExecutionException eex) {
            eex.getMessage();
        } catch (InterruptedException iex) {
            iex.getMessage();
        }
        catch (JSONException jex) {
            jex.getMessage();
        }


        userListAdapter = new UserListAdapter(getActivity(), R.layout.user_item, users);
        lv_users = (ListView) rootView.findViewById(R.id.lv_tweets);
        lv_users.setAdapter(userListAdapter);

        lv_users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (friendsList == true && user.getScreenName().equals(tweetModel.getCurrentUser().getScreenName())) {
                    unfollowAlertButton(i);
                }
            }
        });

        return rootView;
    }

    private void unfollowAlertButton(final int userPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Unfollow");
        builder.setMessage("Are you sure you want to unfollow " + users.get(userPosition).getScreenName() + "?");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                unfollowUser(userPosition);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void unfollowUser(int userPosition) {
        try {
            new UnfollowUser(users.get(userPosition).getScreenName()).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        users.remove(userPosition);
        userListAdapter.notifyDataSetChanged();
        user.friendsRemoved();
        listener.onRefresh();
    }

    private class GetUsers extends AsyncTask<String, Void, String> {
        private String url;
        private String userScreenName;

        public GetUsers(String userScreenName) {
            this.userScreenName = userScreenName;
        }

        @Override
        protected String doInBackground(String... params) {
            getUrl();
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

        private void getUrl() {
            if (friendsList == true) {
                url = "https://api.twitter.com/1.1/friends/list.json?screen_name=" + userScreenName;
            }
            else {
                url = "https://api.twitter.com/1.1/followers/list.json?screen_name=" + userScreenName;
            }
        }
    }

    public class UnfollowUser extends AsyncTask<Boolean, Void, Boolean> {
        private String screenName;

        public UnfollowUser(String userScreenName) {
            screenName = userScreenName;
        }

        @Override
        protected Boolean doInBackground(Boolean... booleen) {
            OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.twitter.com/1.1/friendships/destroy.json?screen_name=" + screenName, tweetModel.getAuthService());
            tweetModel.getAuthService().signRequest(tweetModel.getAccessToken(), request);
            Response response = request.send();
            if (response.isSuccessful()) {
                return true;
            }
            return null;
        }
    }

}