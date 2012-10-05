package com.creatine.socialite.Feed;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;

import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;

public class FeedParser {
	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = null;
	
	private Facebook facebook;
	
	public FeedParser(Facebook facebook) {
		this.facebook = facebook;
	}
	
	public JSONObject getJSONFromGraph(String s) {
		try
        {
            jObj = Util.parseJson(facebook.request(s));
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (FacebookError e)
        {
            e.printStackTrace();
        }
		return jObj;
	}
}