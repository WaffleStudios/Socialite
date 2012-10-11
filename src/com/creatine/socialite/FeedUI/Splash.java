package com.creatine.socialite.FeedUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.creatine.socialite.R;
import com.creatine.socialite.Facebook.FBConnect;
import com.creatine.socialite.Facebook.SessionEvents;

public class Splash extends SherlockActivity {
	
	private static final String APP_ID = "223686664426439";
	private static final String[] PERMISSIONS = { "publish_stream", "read_stream", "user_photos" };
	
	private Button login;
	private FBConnect facebookConnector;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		this.facebookConnector = new FBConnect(this, getApplicationContext(), APP_ID, PERMISSIONS);
		login = (Button) findViewById(R.id.login);
		login.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SessionEvents.AuthListener listener = new SessionEvents.AuthListener() {
					@Override
					public void onAuthSucceed() {
			            System.out.println("AuthSuccess");
						Intent mi = new Intent(getBaseContext(), NewsFeed.class);
			            mi.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			            mi.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			            startActivity(mi);
			        }
					@Override
					public void onAuthFail(String error) {
						Toast.makeText(getBaseContext(), error, Toast.LENGTH_LONG).show();
					}
				};
				SessionEvents.addAuthListener(listener);
				facebookConnector.login();
				}			
		});
	}
}