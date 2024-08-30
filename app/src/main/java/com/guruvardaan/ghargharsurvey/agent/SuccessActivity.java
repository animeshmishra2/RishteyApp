package com.guruvardaan.ghargharsurvey.agent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

public class SuccessActivity extends BaseActivity {

    TextView plot_num, plot_name, plot_price, service_duration, monthly_emi, total_emi, down_payment, my_orders;
    ImageView back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_success, frameLayout);
        back_button = (ImageView) findViewById(R.id.back_button);
        plot_num = (TextView) findViewById(R.id.plot_num);
        plot_name = (TextView) findViewById(R.id.plot_name);
        plot_price = (TextView) findViewById(R.id.plot_price);
        service_duration = (TextView) findViewById(R.id.service_duration);
        monthly_emi = (TextView) findViewById(R.id.monthly_emi);
        my_orders = (TextView) findViewById(R.id.my_orders);
        total_emi = (TextView) findViewById(R.id.total_emi);
        down_payment = (TextView) findViewById(R.id.down_payment);
        plot_name.setText(getIntent().getExtras().getString("PLOTNAME"));
        plot_num.setText("PLOT NAME : " + getIntent().getExtras().getString("PLOTNAME"));
        plot_price.setText("\u20b9" + getIntent().getExtras().getString("PRICE"));
        service_duration.setText(getIntent().getExtras().getString("DURATION"));
        monthly_emi.setText("\u20b9" + getIntent().getExtras().getString("INSTALLMENT"));
        total_emi.setText(getIntent().getExtras().getString("EMI"));
        down_payment.setText("\u20b9" + getIntent().getExtras().getString("DP"));
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
        my_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}