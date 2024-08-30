package com.guruvardaan.ghargharsurvey.welcome;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import com.rishtey.agentapp.R;

public class TermsActivity extends BaseActivity {

    WebView webView;
    ImageView back_b;
    TextView header_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_privacy_policy, frameLayout);
        webView = (WebView) findViewById(R.id.webView);
        header_txt = (TextView) findViewById(R.id.header_txt);
        header_txt.setText(getString(R.string.terms));
        webView.loadUrl("file:///android_asset/terms.html");
        back_b = (ImageView) findViewById(R.id.back_button);
        back_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}