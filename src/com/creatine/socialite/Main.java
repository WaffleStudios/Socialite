package com.creatine.socialite;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.Util;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Main extends SherlockActivity {
	
	public static final String APP_ID = "223686664426439";
	private static final String[] PERMISSIONS = { "publish_stream", "read_stream", "user_photos" };
	private static final String PREFS = "sharedPrefs";
	
	private SharedPreferences mPrefs;
	private long expires;
	private String access_token;
	private Facebook facebook = new Facebook(APP_ID);
	private SharedPreferences.Editor editor;
	private Gson gson;
	
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        /*
         * Gets existing access token, if any.
         */
    	mPrefs = getSharedPreferences(PREFS, MODE_PRIVATE);
    	editor = mPrefs.edit();
        editor.putString("appId", "223686664426439");
        access_token = mPrefs.getString("access_token", null);
        expires = mPrefs.getLong("access_expires", 0);
        if(access_token != null) {
            facebook.setAccessToken(access_token);
        }
        if(expires != 0) {
            facebook.setAccessExpires(expires);
        }
        // If there is no existing access token, it will log in and save access token.
    	if(!facebook.isSessionValid()) {
    		setContentView(R.layout.main);
    		Button signIn = (Button) findViewById(R.id.sign_in);
    		signIn.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					authorize();
					setContentView(R.layout.news_feed);
					assembleNF();
				}
			});
    	} else {
    		setContentView(R.layout.news_feed);
    		assembleNF();
    	}
    }

    public void onResume() {    
        super.onResume();
        facebook.extendAccessTokenIfNeeded(this, null);
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);

        facebook.authorizeCallback(requestCode, resultCode, data);
    }
    
    public void assembleNF() {
    	Intent p = new Intent(this, com.creatine.socialite.FeedUI.NewsFeed.class);
    	p.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	startActivity(p);
	}
    
    public void authorize() {
    	facebook.authorize(this, PERMISSIONS,
				new DialogListener() {
			public void onComplete(Bundle values) {
                editor.putString("access_token", facebook.getAccessToken());
                editor.putLong("access_expires", facebook.getAccessExpires());
                editor.commit();
			}
	    	
	    	public void onFacebookError(FacebookError error) {}
	    	
	    	public void onError(DialogError e) {}
	    	
	    	public void onCancel() {}
	    	
	    });
    }
    /*
     * Menu options
     */
    
}

