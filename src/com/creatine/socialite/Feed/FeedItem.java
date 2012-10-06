package com.creatine.socialite.Feed;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class FeedItem {
	
	private String user;
	private String message;
	private Bitmap userPicture;
	private Bitmap image;
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setUserPicture(String userId) {
		URL img_value = null;
		try {
			img_value = new URL("http://graph.facebook.com/"+userId+"/picture");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			this.userPicture = BitmapFactory.decodeStream(img_value.openConnection().getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setImage(String photoId) {
		URL img_value = null;
		try {
			img_value = new URL(photoId);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			this.image = BitmapFactory.decodeStream(img_value.openConnection().getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getUser() {
		return this.user;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public Bitmap getUserPicture() {
		return userPicture;
	}
	
	public Bitmap getImage() {
		return image;
	}
}