package com.creatine.socialite.FeedUI;

import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.creatine.socialite.R;
import com.creatine.socialite.Facebook.FBConnect;
import com.creatine.socialite.Post.Status;

public class NewsFeed extends SherlockFragmentActivity {
	
	private static final String APP_ID = "223686664426439";
	private static final String[] PERMISSIONS = { "publish_stream", "read_stream", "user_photos" };

	public static FBConnect fbConnect;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NewsFeed.fbConnect = new FBConnect(this, getApplicationContext(), APP_ID, PERMISSIONS);
        setContentView(R.layout.news_feed);
        checkLogin();
    }
    
    public void checkLogin() {
    	if(!fbConnect.getFacebook().isSessionValid()) {
    		startActivity(new Intent(this, Splash.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    	}
    }
    
    public void onResume() {
    	super.onResume();
        fbConnect.getFacebook().extendAccessTokenIfNeeded(this, null);
    }
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuinflate = new MenuInflater(this);
		menuinflate.inflate(R.menu.main, menu);
		return true;
	}	

	public boolean onOptionsItemSelected(MenuItem item) {
	    if (item.getItemId() == R.id.menu_status) {
	    	Intent p = new Intent(this, Status.class);
	    	p.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    	startActivity(p);
	    	return true;
	    } if (item.getItemId() == R.id.menu_settings) {
	    	Intent p = new Intent(this, com.creatine.socialite.Settings.Settings.class);
	    	p.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    	startActivity(p);
	    	return true;
	    }
		return false;
	}
	
}