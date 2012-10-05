package com.creatine.socialite.FeedUI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import com.creatine.socialite.R;
import com.creatine.socialite.Status;
import com.creatine.socialite.Feed.FeedItem;
import com.creatine.socialite.Feed.FeedParser;
import com.facebook.android.Facebook;
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewsFeed extends SherlockListActivity {
	
	private static String newsFeed = "me/home";
	
	private static final String TAG_DATA = "data";
	private static final String TAG_ID = "id";
	private static final String TAG_FROM = "from";
	private static final String TAG_FROM_NAME = "name";
	private static final String TAG_FROM_ID = "id";
	private static final String TAG_MESSAGE = "message";
	private static final String TAG_TYPE = "type";
	
	JSONArray feed = null;

	private Context mContext;
	private Facebook facebook;
	private SharedPreferences mPrefs;
	private ArrayList<FeedItem> items;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_feed);
        mContext = getApplicationContext();
        mPrefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        facebook = new Facebook(mPrefs.getString("appId", null));
        facebook.setAccessToken(mPrefs.getString("access_token", null));
        items = new ArrayList<FeedItem>();
        FeedParser jParser = new FeedParser(facebook);
		 
        JSONObject json = jParser.getJSONFromGraph(newsFeed);
 
        try {
            feed = json.getJSONArray(TAG_DATA);
 
            for(int i = 0; i < feed.length(); i++){
	                JSONObject f = feed.getJSONObject(i);
	                String message = null;
	                String type = f.getString(TAG_TYPE);
		                String id = f.getString(TAG_ID);
		                if(f.has("message")){
		                	message = f.getString(TAG_MESSAGE);
		                }
		 
		                JSONObject from = f.getJSONObject(TAG_FROM);
		                String name = from.getString(TAG_FROM_NAME);
		                String user_id = from.getString(TAG_FROM_ID);
		 
		                FeedItem item = new FeedItem();
		 
		                item.setUser(name);
		                item.setMessage(message);
		                item.setUserPicture(user_id);
		                if(type.equals("status")) {
			                items.add(item);
		                }
            }
            System.out.println("Sync done, setting view");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListAdapter adapter = new FeedAdapter(getBaseContext(), R.layout.feed_items, items);
        setListAdapter(adapter);

        ((PullToRefreshListView) getListView()).setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
				// TODO: Add pull to refresh action
            }
        });
    }
	
	private class FeedAdapter extends ArrayAdapter<FeedItem> {
		
		private ArrayList<FeedItem> items;
		
		public FeedAdapter(Context context, int textViewResourceId, ArrayList<FeedItem> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}
		
		 public View getView(int position, View convertView, ViewGroup parent) {
	         View v = convertView;
	         if (v == null) {
	             LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	             v = vi.inflate(R.layout.feed_items, null);
	         }
	         FeedItem feed = items.get(position);
	         if (feed != null) {
                 ImageView userPicture = (ImageView) v.findViewById(R.id.profile_picture);    
	        	 TextView user = (TextView) v.findViewById(R.id.feed_user);
	             TextView message = (TextView) v.findViewById(R.id.feed_text);
	             if (user != null) {
	                  user.setText(feed.getUser());                            }
	             if(message != null){
	                  message.setText(feed.getMessage());
	             }
	             if(userPicture != null) {
	               userPicture.setImageBitmap(feed.getUserPicture());
	             }
	         }
	         return v;
		 }
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
	    }
		return false;
	}
	
}