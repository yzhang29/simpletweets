package com.codepath.apps.simpletwitter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.simpletwitter.models.Tweet;
import com.codepath.apps.simpletwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;


public class ComposeActivity extends Activity {
    private TwitterClient client;
    private User user;
    private ImageView ivProfile;
    private TextView tvUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        tvUserId = (TextView) findViewById(R.id.tvUserId);
        client = TwitterClientApp.getRestClient();
        ivProfile.setImageResource(android.R.color.transparent);
        populateComposeUser();
    }

    public void populateComposeUser(){
        client.getUserInfo(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(JSONObject json) {
                user = User.fromJson(json);
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.displayImage(user.getProfileImageUrl(), ivProfile);
                tvUserId.setText("@"+user.getScreenName());
            }

            @Override
            public void onFailure(Throwable e, String s) {
                super.onFailure(e, s);
                Log.d("debug", e.toString());
                Log.d("debug", s.toString());
            }
        });
    }

    public void postTweet(View view){
        EditText etStatus = (EditText) findViewById(R.id.etStatus);
        String status = etStatus.getText().toString();
        Toast.makeText(this, "Posting your tweets...", Toast.LENGTH_SHORT).show();
        client.postTweet(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(JSONObject json) {
                Log.d("debug", "Success");
                Intent data = new Intent();
                setResult(RESULT_OK, data);
                finish();
            }

            @Override
            public void onFailure(Throwable e, String s) {
                super.onFailure(e, s);
                Log.d("debug", e.toString());
                Log.d("debug", s.toString());
            }
        }, status);
    }
}
