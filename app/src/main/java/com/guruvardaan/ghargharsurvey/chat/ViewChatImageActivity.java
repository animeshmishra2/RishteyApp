package com.guruvardaan.ghargharsurvey.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.jsibbold.zoomage.ZoomageView;
import com.rishtey.agentapp.R;

public class ViewChatImageActivity extends AppCompatActivity {

    ImageView back_button;
    ZoomageView myZoomageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_chat_image);
        back_button = (ImageView) findViewById(R.id.back_button);
        myZoomageView = (ZoomageView) findViewById(R.id.myZoomageView);
        Glide.with(getApplicationContext())
                .asBitmap()
                .load(getIntent().getExtras().getString("IMG"))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap,
                                                Transition<? super Bitmap> transition) {
                        /*int w = bitmap.getWidth();
                        int h = bitmap.getHeight();*/
                        myZoomageView.setImageBitmap(bitmap);
                    }
                });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}