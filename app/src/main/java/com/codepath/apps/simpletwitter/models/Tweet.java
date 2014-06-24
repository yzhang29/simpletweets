package com.codepath.apps.simpletwitter.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by yzhang29 on 6/22/14.
 */
public class Tweet {
    private String body;
    private long uid;
    private String createdAt;
    private User user;

    public static Tweet fromJson(JSONObject jsonObject){
        Tweet tweet = new Tweet();
        try{
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJsonArray(JSONArray jsonArray){
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());
        for(int i = 0 ; i<jsonArray.length(); i++){
            JSONObject tweetJson = null;
            try{
                tweetJson = jsonArray.getJSONObject(i);
            }catch (Exception e){
                e.printStackTrace();
                continue;
            }
            Tweet tweet = Tweet.fromJson(tweetJson);
            if(tweet!=null){
                tweets.add(tweet);
            }
        }
        return tweets;
    }

    @Override
    public String toString() {
        return getBody()+" - " + getUser().getScreenName();
    }

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }
}
