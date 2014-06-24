package com.codepath.apps.simpletwitter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.codepath.apps.simpletwitter.R;
import com.codepath.apps.simpletwitter.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.concurrent.CompletionService;

public class HomeTimelineActivity extends Activity {
    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private ArrayAdapter<Tweet> aTweets;
    private ListView lvTweets;
    private static final int REQUEST_CODE=20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_timeline);
        client = TwitterClientApp.getRestClient();
        populateTimeline();
        lvTweets = (ListView) findViewById(R.id.lvTweets);
        tweets = new ArrayList<Tweet>();
        aTweets = new TweetArrayAdapter(this, tweets);
        lvTweets.setAdapter(aTweets);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });
    }
    // Append more data into the adapter
    public void customLoadMoreDataFromApi(int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter
        long id = tweets.get(tweets.size()-1).getUid()-1;
        client.getHomeTimeLine(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(JSONArray json) {
                aTweets.addAll(Tweet.fromJsonArray(json));
            }

            @Override
            public void onFailure(Throwable e, String s) {
                super.onFailure(e, s);
                Log.d("debug", e.toString());
                Log.d("debug", s.toString());
            }
        }, Long.toString(id));
    }
    public void populateTimeline(){
        client.getHomeTimeLine(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(JSONArray json) {
                Log.d("json", json.toString());
                aTweets.addAll(Tweet.fromJsonArray(json));
            }

            @Override
            public void onFailure(Throwable e, String s) {
                super.onFailure(e, s);
                Log.d("debug", e.toString());
                Log.d("debug", s.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_timeline, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
                Intent refresh = new Intent(this, HomeTimelineActivity.class);
                startActivity(refresh);
                this.finish();
        }
    }

    public void onComposeAction(MenuItem mi) {
        Intent i = new Intent(HomeTimelineActivity.this, ComposeActivity.class);
        startActivityForResult(i, REQUEST_CODE);
    }
}
