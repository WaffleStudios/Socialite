package com.creatine.socialite.Facebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.creatine.socialite.Facebook.SessionEvents.AuthListener;
import com.creatine.socialite.Facebook.SessionEvents.LogoutListener;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;

public class FBConnect {
	/*
	 * Connector class and all sub-classes created by R2DoesInc
	 * Source available at http://github.com/t3hh4xx0r/TweeZee/
	 * Follow on Twitter @r2doesinc
	 * 
	 * -Creatine Development
	 */
	
	private Facebook facebook = null;
	private String[] permissions;
	private Handler handler;
	private Activity activity;
	private Context context;
	private SessionListener sessionListener = new SessionListener();
	
	public FBConnect(Activity activity, Context context, String appId, String[] permissions) {
		this.facebook = new Facebook(appId);
		SessionStore.restore(facebook, context);
        SessionEvents.addAuthListener(sessionListener);
        SessionEvents.addLogoutListener(sessionListener);
		this.activity = activity;
		this.context = context;
		this.permissions = permissions;
		this.handler = new Handler();
	}
	
	public void login() {
        if (!facebook.isSessionValid()) {
        	facebook.authorize(this.activity, this.permissions,
    				Facebook.FORCE_DIALOG_AUTH, new LoginDialogListener());
        }
    }
	
	public void logout() {
        SessionEvents.onLogoutBegin();
        AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(this.facebook);
        asyncRunner.logout(this.context, new LogoutRequestListener());
	}
	
	private final class LoginDialogListener implements DialogListener {
        public void onComplete(Bundle values) {
            SessionEvents.onLoginSuccess();
        }

        public void onFacebookError(FacebookError error) {
            SessionEvents.onLoginError(error.getMessage());
        }
        
        public void onError(DialogError error) {
            SessionEvents.onLoginError(error.getMessage());
        }

        public void onCancel() {
            SessionEvents.onLoginError("Action Canceled");
        }
    }
	
	public class LogoutRequestListener extends BaseRequestListener {
        public void onComplete(String response, final Object state) {
            // callback should be run in the original thread, 
            // not the background thread
            handler.post(new Runnable() {
                public void run() {
                    SessionEvents.onLogoutFinish();
                }
            });
        }
    }
	
	private class SessionListener implements AuthListener, LogoutListener {
        
        public void onAuthSucceed() {
        	SessionStore.save(facebook, context);
        }

        public void onAuthFail(String error) {
        }
        
        public void onLogoutBegin() {           
        }
        
        public void onLogoutFinish() {
        	SessionStore.clear(context);
            Intent i = new Intent(context, com.creatine.socialite.FeedUI.NewsFeed.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(i);
        }
    }
	
	public Facebook getFacebook() {
		return this.facebook;
	}
}