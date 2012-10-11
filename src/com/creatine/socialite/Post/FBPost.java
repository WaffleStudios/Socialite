package com.creatine.socialite.Post;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.creatine.socialite.Facebook.FBConnect;



public class FBPost {
	
	private FBConnect fbConnect;
	
	public FBPost(Activity activity, Context context, String appId, String[] permissions) {
		this.fbConnect = new FBConnect(activity, context, appId, permissions);
	}
	
	public void updateStatus(final String status) {
		new Thread(new Runnable () {
			public void run() {
				Bundle parameters = new Bundle();
				parameters.putString("message", status);  
				try {
					String response = fbConnect.getFacebook().request("me/feed", parameters,"POST");
					System.out.println(response);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public void postPhoto(final ByteArrayOutputStream bos, final String status) {
		new Thread(new Runnable () {
	    	public void run() {
	    		Bundle parameters = new Bundle();
	    	    if(status != null) {
	    	    	parameters.putString("message", status);
	    	    }
	    	    parameters.putByteArray("picture", bos.toByteArray());
	    		try {
	    			String response = fbConnect.getFacebook().request("me/photos", parameters,"POST");
	    			System.out.println(response);
	    		} catch (IOException e) {
	    			e.printStackTrace();
	    		}
	    	}
	    }).start();
	}
}