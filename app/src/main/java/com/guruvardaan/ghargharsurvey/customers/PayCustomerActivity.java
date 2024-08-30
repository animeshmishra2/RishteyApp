package com.guruvardaan.ghargharsurvey.customers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;
import com.guruvardaan.ghargharsurvey.welcome.SelectServiceActivity;

public class PayCustomerActivity extends BaseActivity {

    ImageView back_button;
    EditText select_service;
    LinearLayout select_service_lay;
    TextView get_due_amount;
    UserDetails userDetails;
    String sid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_pay_customer, frameLayout);
        sid = "";
        back_button = (ImageView) findViewById(R.id.back_button);
        get_due_amount = (TextView) findViewById(R.id.get_due_amount);
        select_service = (EditText) findViewById(R.id.select_service);
        userDetails = new UserDetails(getApplicationContext());
        select_service_lay = (LinearLayout) findViewById(R.id.select_service_lay);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        select_service_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), SelectServiceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("SID", userDetails.getuserid());
                intent.putExtras(bundle);
                startActivityForResult(intent, 23);
            }
        });
        get_due_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), InstallmentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("CID", userDetails.getuserid());
                bundle.putString("SID", sid);
                bundle.putString("NAME", userDetails.getName());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 23 && resultCode == RESULT_OK) {
            sid = data.getExtras().getString("ID");
            select_service.setText(data.getExtras().getString("NAME"));
        }
    }
}