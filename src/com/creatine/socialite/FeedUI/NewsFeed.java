package com.creatine.socialite.FeedUI;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.creatine.socialite.R;
import com.creatine.socialite.Status;
import com.creatine.socialite.Feed.FeedItem;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;

public class NewsFeed extends SherlockListActivity {
	
	private static String newsFeed = "me/home";
	
	private static final String TAG_DATA = "data";
	private static final String TAG_ID = "id";
	private static final String TAG_FROM = "from";
	private static final String TAG_FROM_NAME = "name";
	private static final String TAG_FROM_ID = "id";
	private static final String TAG_MESSAGE = "message";
	private static final String TAG_PICTURE = "picture";
	private static final String TAG_TYPE = "type";

	private Context mContext;
	private Facebook facebook;
	private SharedPreferences mPrefs;
	private ArrayList<FeedItem> items;
	private FeedAdapter mAdapter = null;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_feed);
        mContext = getApplicationContext();
        mPrefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        facebook = new Facebook(mPrefs.getString("appId", null));
        facebook.setAccessToken(mPrefs.getString("access_token", null));
        items = new ArrayList<FeedItem>();
		
        new GetFeedTask().execute(newsFeed);

        ((PullToRefreshListView) getListView()).setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
				new GetFeedTask().execute(newsFeed);
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
	}
	
	private class GetFeedTask extends AsyncTask<String, Void, FeedAdapter> {

		@Override
		protected void onPreExecute() {
			if(mAdapter != null) {
				mAdapter.notifyDataSetChanged();
			}
		}
		@Override
		protected FeedAdapter doInBackground(String... params) {
			JSONObject json = null;
			JSONArray feed = null;
	        try {
	            json = Util.parseJson(facebook.request(params[0]));
	        } catch (MalformedURLException e) {
	            e.printStackTrace();
	        } catch (JSONException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (FacebookError e) {
	            e.printStackTrace();
	        }
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
			        if(type.equals("photo")) {
			        	String photo = f.getString(TAG_PICTURE);
			        	if(f.has("story") && !f.has("message")) {
			        		message = f.getString("story");
			        	}
			        	item.setImage(photo);
			        }
			        item.setUser(name);
			        item.setMessage(message);
			        item.setUserPicture(user_id);
			        if(!type.equals("link")) {
			        	items.add(item);
			        }
	            }
	            System.out.println("Sync done, setting view");
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	        mAdapter = new FeedAdapter(getBaseContext(), R.layout.feed_items, items);
	        
			return mAdapter;
		}
		
		@Override
		protected void onPostExecute(FeedAdapter fAdapter) {
			ListAdapter adapter = fAdapter;
	        setListAdapter(adapter);
			((PullToRefreshListView) getListView()).onRefreshComplete();
			adapter = null;
			super.onPostExecute(fAdapter);
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