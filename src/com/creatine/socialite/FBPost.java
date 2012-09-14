package com.creatine.socialite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.android.Facebook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;



public class FBPost {
	
	private Facebook facebook;
	
	public FBPost(String appId, String access_token) {
		facebook = new Facebook(appId);
		facebook.setAccessToken(access_token);
	}
	
	public void updateStatus(final String status) {
		new Thread(new Runnable () {
			public void run() {
				Bundle parameters = new Bundle();
				parameters.putString("message", status);  
				try {
					String response = facebook.request("me/feed", parameters,"POST");
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
	    			String response = facebook.request("me/photos", parameters,"POST");
	    			System.out.println(response);
	    		} catch (IOException e) {
	    			e.printStackTrace();
	    		}
	    	}
	    }).start();
	}
}