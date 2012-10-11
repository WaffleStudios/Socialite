package com.creatine.socialite.Feed;

import java.util.ArrayList;

import com.creatine.socialite.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FeedAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<FeedItem> items;
	
	public FeedAdapter(Activity activity, int textViewResourceId, ArrayList<FeedItem> items) {
		this.items = items;
		this.activity = activity;
	}
	
	 public View getView(int position, View convertView, ViewGroup parent) {
         View v = convertView;
         if (v == null) {
             LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
             v = vi.inflate(R.layout.feed_items, null);
         }
         FeedItem feed = items.get(position);
         if (feed != null) {
             ImageView userPicture = (ImageView) v.findViewById(R.id.profile_picture);    
        	 TextView user = (TextView) v.findViewById(R.id.feed_user);
             TextView message = (TextView) v.findViewById(R.id.feed_text);
             ImageView image = (ImageView) v.findViewById(R.id.feed_photo);
             if(userPicture != null) {
	             userPicture.setImageBitmap(feed.getUserPicture());
	         }
             if (user != null) {
                 user.setText(feed.getUser());                            
             }
             if(message != null){
                 message.setText(feed.getMessage());
             }
             if(image != null) {
            	 image.setImageBitmap(feed.getImage());
             }
         }
         return v;
	 }

	public int getCount() {
		return items.size();
	}

	public Object getItem(int i) {
		return i;
	}

	public long getItemId(int i) {
		return i;
	}
	
}