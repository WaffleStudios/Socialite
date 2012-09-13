package com.creatine.socialite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import java.io.File;

public class Status extends SherlockActivity {

	private static final int SELECT_PICTURE = 1;
	private static final int CAMERA_PICTURE = 2;

	private FBPost post;
	private SharedPreferences mPrefs;
	private String selectedImagePath;
	private String access_token;
	private Uri imageUri;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.status);
		mPrefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
		access_token = mPrefs.getString("access_token", null);
		post = new FBPost(Main.APP_ID, access_token);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/*
		 * Gets picture path based on method of entry
		 */
		if (requestCode == SELECT_PICTURE) {
            if(data != null) {
            	Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
            }
        }
	    if (resultCode == CAMERA_PICTURE) {
	            Uri selectedImageUri = data.getData();
	            selectedImagePath = getPath(selectedImageUri);
	    }
	}
	// Retrieves path from Uri to allow for upload
	public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
	// Creates menu
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuinflate = new MenuInflater(this);
		menuinflate.inflate(R.menu.status, menu);
		return true;
	}	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.post_status) {
	    	EditText edit = (EditText) findViewById(R.id.status_entry);
        	String status = edit.getText().toString();
        	if(selectedImagePath != null) {
        		post.postPhoto(selectedImagePath, status);
        		selectedImagePath = null;
        	} else {
        		post.updateStatus(status);
        	}
        	Intent home = new Intent(this, Main.class);
    		home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		startActivity(home);
    		Context context = getApplicationContext();
    		int duration = Toast.LENGTH_SHORT;
    		Toast toast = Toast.makeText(context, "Successfully posted", duration);
    		toast.show();
	    	return true;
	    }
		if (item.getItemId() == R.id.add_photo) {
			Intent selectPhoto = new Intent(Intent.ACTION_PICK,
		               android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(selectPhoto, SELECT_PICTURE);
			return true;
		}
	    if (item.getItemId() == R.id.take_photo) {
	    	Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
	    	File photo = new File(Environment.getExternalStorageDirectory(),  "pic.jpg");
	    	cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
	                Uri.fromFile(photo));
	    	imageUri = Uri.fromFile(photo);
	    	selectedImagePath = getPath(imageUri);
            return true;
	    }
		switch (item.getItemId()) {
        case android.R.id.home:
            Intent intent = new Intent(this, Main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
		}
	}
}