package com.codepath.apps.simpletwitter;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "LDlTIJgJVorfjsXNS7Kba9jBb";       // Change this
    public static final String REST_CONSUMER_SECRET = "4sJaxXuo4aIGdByg5w0SqVxDrrRvkcFNwhKuuWutNEIBaayWcw"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets"; // Change this (here and in manifest)
    
    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    public void getHomeTimeLine(String maxId, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("since_id", "1");
        params.put("count", "20");
        if(maxId!=null){
            params.put("max_id", maxId);
        }
        client.get(apiUrl, params, handler);
    }

    public void getMentionsTimeLine(String maxId, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        params.put("since_id", "1");
        params.put("count", "20");
        if(maxId!=null){
            params.put("max_id", maxId);
        }
        client.get(apiUrl, params, handler);
    }

    public void getUserInfo(AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("account/verify_credentials.json");
        client.get(apiUrl, null, handler);
    }

    public void getOtherUserInfo(String userScreenName, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("users/show.json");
        RequestParams params = new RequestParams();
        params.put("screen_name", userScreenName);
        client.get(apiUrl, params, handler);
    }

    public void getUserTimeline(String userScreenName, String maxId, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        if(userScreenName!=null && !userScreenName.isEmpty()){
            params.put("screen_name", userScreenName);
        }
        if(maxId!=null){
            params.put("max_id", maxId);
        }
        params.put("since_id", "1");
        params.put("count", "20");
        client.get(apiUrl, params, handler);
    }

    public void postTweet(AsyncHttpResponseHandler handler, String status){
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", status);
        client.post(apiUrl, params, handler);
    }
}