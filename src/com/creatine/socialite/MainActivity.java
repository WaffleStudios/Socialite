package com.creatine.socialite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.creatine.socialite.R;
import com.creatine.socialite.R.layout;
import com.facebook.android.*;
import com.facebook.android.Facebook.*;

public class MainActivity extends Activity {

    Facebook facebook = new Facebook("350758635005869");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        facebook.authorize(this, new DialogListener() {
            
            public void onComplete(Bundle values) {}

            public void onFacebookError(FacebookError error) {}

            public void onError(DialogError e) {}

            public void onCancel() {}
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        facebook.authorizeCallback(requestCode, resultCode, data);
    }
}