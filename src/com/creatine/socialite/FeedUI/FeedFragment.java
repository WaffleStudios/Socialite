package com.creatine.socialite.FeedUI;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.creatine.socialite.R;
import com.creatine.socialite.Facebook.FBConnect;
import com.creatine.socialite.Feed.FeedAdapter;
import com.creatine.socialite.Feed.FeedItem;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;

public class FeedFragment extends SherlockFragment {
	
	private static final String APP_ID = "223686664426439";
	private static final String[] PERMISSIONS = { "publish_stream", "read_stream", "user_photos" };
	private static final String NEWS = "me/home";
	
	private static final String TAG_DATA = "data";
	private static final String TAG_ID = "id";
	private static final String TAG_FROM = "from";
	private static final String TAG_FROM_NAME = "name";
	private static final String TAG_FROM_ID = "id";
	private static final String TAG_MESSAGE = "message";
	private static final String TAG_PICTURE = "picture";
	private static final String TAG_TYPE = "type";
	private static final String TAG_CREATED_TIME = "created_time";
	
	private ArrayList<FeedItem> items = new ArrayList<FeedItem>();
	FeedItem item;
	private FeedAdapter mAdapter;
	private FBConnect fbConnect;
	private PullToRefreshListView listView;
	private Bundle param = new Bundle();
	private String time;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fbConnect = new FBConnect(getActivity(), getActivity().getBaseContext(), APP_ID, PERMISSIONS);
		if(fbConnect.getFacebook().isSessionValid()) {
    		new GetFeedTask().execute(NEWS);
    	}
		setRetainInstance(true);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		listView = new PullToRefreshListView(getActivity());
		listView.setAdapter(mAdapter);
		listView.setOnRefreshListener(new OnRefreshListener() {
		    @Override
		    public void onRefresh() {
		        param.putString("since", time);
		    	new GetFeedTask().execute(NEWS);
		        mAdapter.notifyDataSetChanged();
		    }
		});
		return listView;
	}
	
	private class GetFeedTask extends AsyncTask<String, Void, FeedAdapter> {
		
		@Override
		protected FeedAdapter doInBackground(String... params) {
			param.putString("limit", "75");
			JSONObject json = null;
			JSONArray feed = null;
	        try{
	        	json = Util.parseJson(fbConnect.getFacebook().request(params[0], param));
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
		            if(i == 0) {
		            	time = f.getString(TAG_CREATED_TIME);
		            }
		            String type = f.getString(TAG_TYPE);
			        String id = f.getString(TAG_ID);
			        if(f.has("message")){
			        	message = f.getString(TAG_MESSAGE);
			        }
			 
			        JSONObject from = f.getJSONObject(TAG_FROM);
			        String name = from.getString(TAG_FROM_NAME);
			        String user_id = from.getString(TAG_FROM_ID);
			        
			        item = new FeedItem();
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
			        items.add(i, item); 
	            }
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	        mAdapter = new FeedAdapter(getActivity(), R.layout.feed_items, items);
			return mAdapter;
		}
		@Override
		public void onPostExecute(FeedAdapter mAdapter) {
			listView.setAdapter(mAdapter);
			listView.onRefreshComplete();
			super.onPostExecute(mAdapter);
		}
	}
}