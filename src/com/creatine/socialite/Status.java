package com.creatine.socialite;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class Status extends SherlockActivity {

	private static final int SELECT_PICTURE = 1;
	private static final int CAMERA_PICTURE = 2;

	private ByteArrayOutputStream bos;
	private FBPost post;
	private SharedPreferences mPrefs;
	private String imagePath;
	private String access_token;
	private Uri imageUri;
	private Uri selectedImage;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.status);
		mPrefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
		access_token = mPrefs.getString("access_token", null);
		post = new FBPost(Main.APP_ID, access_token);
		ImageButton status = (ImageButton) findViewById(R.id.post_status);
		ImageButton photo = (ImageButton) findViewById(R.id.add_photo);
		ImageButton camera = (ImageButton) findViewById(R.id.take_photo);
		ImageButton checkIn = (ImageButton) findViewById(R.id.attach_location);
		status.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				EditText edit = (EditText) findViewById(R.id.status_entry);
	        	String status = edit.getText().toString();
	        	if(imagePath != null) {
	        		post.postPhoto(bos, status);
	        		imagePath = null;
	        	} else {
	        		post.updateStatus(status);
	        	}
	        	Intent home = new Intent(getBaseContext(), Main.class);
	    		home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    		startActivity(home);
			}
		});
		photo.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent selectPhoto = new Intent(Intent.ACTION_PICK,
			               android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(selectPhoto, SELECT_PICTURE);
			}
		});
		camera.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				String fileName = "socialite-temp.jpg";
				ContentValues values = new ContentValues();
				values.put(MediaStore.Images.Media.TITLE, fileName);
				values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");
				imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			    startActivityForResult(intent, CAMERA_PICTURE);
			}
		});
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/*
		 * Gets picture path based on method of entry
		 */
		if (requestCode == SELECT_PICTURE) {
            if(data != null) {
            	selectedImage = data.getData();
                imagePath = getPath(selectedImage);
                bos = new ByteArrayOutputStream();
	    		Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
	    	    bitmap.compress(CompressFormat.JPEG, 100, bos);
            }
        }
	    if (requestCode == CAMERA_PICTURE) {
	        imagePath = getPath(imageUri);
	    	bos = new ByteArrayOutputStream();
	    	Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
	    	bitmap.compress(CompressFormat.JPEG, 100, bos);
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
	/* Outdated.  Slowly working it out.
	    if (item.getItemId() == R.id.take_photo) {
	    	
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
	*/
}