/*package com.creatine.socialite;

import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook;

public class PostStatus extends Activity {
	Facebook facebook = new Facebook("223686664426439");
	
	private int mAuthAttempts = 0;
	private String mFacebookToken;
	
	private void fbAuthAndPost(final String message){

	    facebook.authorize(this., new String[]{"publish_stream"}, new DialogListener() {

	       
	        public void onComplete(Bundle values) {
	            Log.d(this.getClass().getName(),"Facebook.authorize Complete: ");
	            saveFBToken(facebook.getAccessToken(), facebook.getAccessExpires());
	            updateStatus(values.getString(Facebook.TOKEN), message);
	        }

	        
	        public void onFacebookError(FacebookError error) {
	            Log.d(this.getClass().getName(),"Facebook.authorize Error: "+error.toString());
	        }

	       
	        public void onError(DialogError e) {
	            Log.d(this.getClass().getName(),"Facebook.authorize DialogError: "+e.toString());
	        }

	        
	        public void onCancel() {
	            Log.d(this.getClass().getName(),"Facebook authorization canceled");
	        }
	    });
	}
	
	
	
	private void showToast(String message){
	    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}
}
*/