package com.creatine.socialite.Post;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.WazaBe.HoloEverywhere.Toast;
import com.actionbarsherlock.app.SherlockActivity;
import com.creatine.socialite.R;

public class Status extends SherlockActivity {

	private static final int SELECT_PICTURE = 1;
	private static final int CAMERA_PICTURE = 2;
	private static final String APP_ID = "223686664426439";
	private static final String[] PERMISSIONS = { "publish_stream", "read_stream", "user_photos" };

	private ByteArrayOutputStream bos;
	private FBPost post;
	private String imagePath;
	private Uri imageUri;
	private Uri selectedImage;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.status);
		post = new FBPost(this, getApplicationContext(), APP_ID, PERMISSIONS);
		ImageButton status = (ImageButton) findViewById(R.id.post_status);
		ImageButton photo = (ImageButton) findViewById(R.id.add_photo);
		ImageButton camera = (ImageButton) findViewById(R.id.take_photo);
		ImageButton location = (ImageButton) findViewById(R.id.attach_location);
		// ImageButton checkIn = (ImageButton) findViewById(R.id.attach_location);
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
	        	finish();
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
		location.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Toast toast = Toast.makeText(getApplicationContext(), "Feature not yet enabled", android.widget.Toast.LENGTH_SHORT);
				toast.show();
			}
		});
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/*
		 * Gets picture path based on method of entry
		 */
		// TODO:  Add Image preview to post dialog.
		if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
            	selectedImage = data.getData();
                imagePath = getPath(selectedImage);
                bos = new ByteArrayOutputStream();
	    		Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
	    	    bitmap.compress(CompressFormat.PNG, 100, bos);
        }
	    if (requestCode == CAMERA_PICTURE && resultCode == Activity.RESULT_OK) {
	        	imagePath = getPath(imageUri);
	        	bos = new ByteArrayOutputStream();
		    	Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
		    	bitmap.compress(CompressFormat.PNG, 100, bos);
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
}