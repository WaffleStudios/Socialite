package com.creatine.socialite;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;


public class Main extends Activity {
	
	private static final String APP_ID = "223686664426439";
	private static final String[] PERMISSIONS = { "email", "publish_stream", "offline_access" };
	
	Facebook facebook = new Facebook(Main.APP_ID);
	private SharedPreferences mPrefs;
	
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);
    	/*
         * Get existing access_token if any
         */
        mPrefs = getPreferences(MODE_PRIVATE);
        String access_token = mPrefs.getString("access_token", null);
        long expires = mPrefs.getLong("access_expires", 0);
        if(access_token != null) {
            facebook.setAccessToken(access_token);
        }
        if(expires != 0) {
            facebook.setAccessExpires(expires);
        }
    	if(!facebook.isSessionValid()) {
    		facebook.authorize(this, Main.PERMISSIONS,
    				new DialogListener() {
    			public void onComplete(Bundle values) {
    				SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putString("access_token", facebook.getAccessToken());
                    editor.putLong("access_expires", facebook.getAccessExpires());
                    editor.commit();
    			}
    	    	
    	    	public void onFacebookError(FacebookError error) {}
    	    	
    	    	public void onError(DialogError e) {}
    	    	
    	    	public void onCancel() {}
    	    	
    	    });
    	}
    }
    // Extends the access token, if necessary.
    public void onResume() {    
        super.onResume();
        facebook.extendAccessTokenIfNeeded(this, null);
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);

        facebook.authorizeCallback(requestCode, resultCode, data);
    }
    
    // Updates Status
    public void updateStatus(){
    	// post on user's wall.
        // facebook.dialog(context, "feed", new PostDialogListener());
    }
}
