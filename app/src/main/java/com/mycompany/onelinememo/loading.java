package com.mycompany.onelinememo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by LG on 2015-07-29.
 */
public class loading extends Activity {

    Intent targetintent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        targetintent = new Intent(loading.this, MainActivity.class);
        startActivityWithDelay();
    }

    public void startActivityWithDelay(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            public void run() {
                startActivity( targetintent );
                finish();
            }
            //Do Something 1000 = 1s
        }, 1000);
    }

}

