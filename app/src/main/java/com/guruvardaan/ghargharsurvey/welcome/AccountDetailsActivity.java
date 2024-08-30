package com.guruvardaan.ghargharsurvey.welcome;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import com.rishtey.agentapp.R;

public class AccountDetailsActivity extends BaseActivity {

    RadioGroup radioGroup1;
    LinearLayout account_lay;
    ImageView qr_img, back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_account_details, frameLayout);
        radioGroup1 = (RadioGroup)findViewById(R.id.radioGroup1);
        account_lay = (LinearLayout) findViewById(R.id.account_lay);
        back_button = (ImageView) findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        qr_img = (ImageView) findViewById(R.id.qr_img);
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radioQR) {
                    qr_img.setVisibility(View.VISIBLE);
                    account_lay.setVisibility(View.GONE);
                } else if (i == R.id.radioAccount) {
                    qr_img.setVisibility(View.GONE);
                    account_lay.setVisibility(View.VISIBLE);
                }
            }
        });
        radioGroup1.check(R.id.radioAccount);
    }
}