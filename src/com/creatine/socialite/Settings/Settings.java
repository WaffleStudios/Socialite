package com.creatine.socialite.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.creatine.socialite.R;
import com.creatine.socialite.Facebook.FBConnect;
import com.creatine.socialite.Facebook.SessionEvents;
import com.creatine.socialite.FeedUI.NewsFeed;

public class Settings extends SherlockPreferenceActivity {

	private static final String APP_ID = "223686664426439";
	private static final String[] PERMISSIONS = { "publish_stream", "read_stream", "user_photos" };
	
	private Preference logout;
	private FBConnect fbConnect;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.settings);
		fbConnect = new FBConnect(this, getApplicationContext(), APP_ID, PERMISSIONS);
		logout = (Preference)findPreference("logout");
		logout.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				SessionEvents.LogoutListener listener = new SessionEvents.LogoutListener() {
					@Override
					public void onLogoutBegin() {
						
					}
					@Override
					public void onLogoutFinish() {
						Toast.makeText(getApplicationContext(), "Logout Successful", Toast.LENGTH_LONG);
					}
				};
				SessionEvents.addLogoutListener(listener);
				fbConnect.logout();
				return false;
			}
		});
	}
}