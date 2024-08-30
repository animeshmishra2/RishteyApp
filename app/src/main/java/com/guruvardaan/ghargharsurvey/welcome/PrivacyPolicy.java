package com.guruvardaan.ghargharsurvey.welcome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import com.rishtey.agentapp.R;

public class PrivacyPolicy extends BaseActivity {

    WebView webView;
    ImageView back_b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_privacy_policy, frameLayout);
        webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/privacy_policy.html");
        back_b = (ImageView) findViewById(R.id.back_button);
        back_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}