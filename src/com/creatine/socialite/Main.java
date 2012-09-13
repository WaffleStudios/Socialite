package com.creatine.socialite;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;


public class Main extends SherlockActivity {
	
	public static final String APP_ID = "223686664426439";
	private static final String[] PERMISSIONS = { "publish_stream", "user_photos" };
	private static final String PREFS = "sharedPrefs";
	
	private SharedPreferences mPrefs;
	private long expires;
	String access_token;
	Facebook facebook = new Facebook(APP_ID);
	
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);
        /*
         * Gets existing access token, if any.
         */
    	mPrefs = getSharedPreferences(PREFS, MODE_PRIVATE);
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
    		facebook.authorize(this, Main.PERMISSIONS,
    				new DialogListener() {
    			public void onComplete(Bundle values) {
    				SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putString("access_token", facebook.getAccessToken());
                    editor.putLong("access_expires", facebook.getAccessExpires());
                    editor.putString("appId", "223686664426439");
                    editor.commit();
    			}
    	    	
    	    	public void onFacebookError(FacebookError error) {}
    	    	
    	    	public void onError(DialogError e) {}
    	    	
    	    	public void onCancel() {}
    	    	
    	    });
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
    /*
     * Menu options
     */
    public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuinflate = new MenuInflater(this);
		menuinflate.inflate(R.menu.main, menu);
		return true;
	}	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    if (item.getItemId() == R.id.menu_status) {
	    	Intent p = new Intent(this, Status.class);
	    	p.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    	startActivity(p);
	    	return true;
	    }
		return false;
	}
}

