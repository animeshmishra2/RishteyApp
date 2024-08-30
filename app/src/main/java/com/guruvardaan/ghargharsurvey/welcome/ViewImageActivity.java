package com.guruvardaan.ghargharsurvey.welcome;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.rishtey.agentapp.R;
import com.jsibbold.zoomage.ZoomageView;

public class ViewImageActivity extends BaseActivity {

    ImageView back_button;
    ZoomageView myZoomageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_view_image, frameLayout);
        back_button = (ImageView) findViewById(R.id.back_button);
        myZoomageView = (ZoomageView) findViewById(R.id.myZoomageView);
        /*Glide.with(myZoomageView.getContext())
                .load(getIntent().getExtras().getString("IMG")).placeholder(R.drawable.albums)
                .into(myZoomageView);*/
        Glide.with(getApplicationContext())
                .asBitmap()
                .load(getIntent().getExtras().getString("IMG"))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap,
                                                Transition<? super Bitmap> transition) {
                        int w = bitmap.getWidth();
                        int h = bitmap.getHeight();
                        myZoomageView.setImageBitmap(bitmap);
                    }
                });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}