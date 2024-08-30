package com.guruvardaan.ghargharsurvey.agent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;
import com.guruvardaan.ghargharsurvey.welcome.LatestOfferActivity;
import com.guruvardaan.ghargharsurvey.welcome.SelectCustomerActivity;
import com.guruvardaan.ghargharsurvey.welcome.SelectServiceActivity;

public class ViewSettlementActivity extends BaseActivity {

    LinearLayout select_cust_lay, select_service_lay;
    EditText select_customer, select_service;
    ImageView back_button;
    String c_id = "";
    String sid = "";
    TextView get_due_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_view_settlement, frameLayout);
        c_id = "";
        sid = "";
        select_cust_lay = (LinearLayout) findViewById(R.id.select_cust_lay);
        select_service_lay = (LinearLayout) findViewById(R.id.select_service_lay);
        select_customer = (EditText) findViewById(R.id.select_customer);
        select_service = (EditText) findViewById(R.id.select_service);
        get_due_amount = (TextView) findViewById(R.id.get_due_amount);
        back_button = (ImageView) findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        select_cust_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectCustomerActivity.class);
                startActivityForResult(intent, 22);
            }
        });
        select_service_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (c_id.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Select Customer", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), SelectServiceActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("SID", c_id);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 23);
                }
            }
        });
        select_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectCustomerActivity.class);
                startActivityForResult(intent, 22);
            }
        });
        select_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (c_id.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Select Customer", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), SelectServiceActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("SID", c_id);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 23);
                }
            }
        });
        get_due_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (c_id.equalsIgnoreCase(" ") || sid.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Select Customer and Service", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = null;
                    if (getIntent().getExtras().getString("OLD").equalsIgnoreCase("1")) {
                        intent = new Intent(getApplicationContext(), LatestOfferActivity.class);
                    } else if (getIntent().getExtras().getString("OLD").equalsIgnoreCase("2")) {
                        intent = new Intent(getApplicationContext(), SettlementActivity.class);
                    } else {
                        intent = new Intent(getApplicationContext(), FourLakhActivity.class);
                    }

                    //Intent intent = new Intent(getApplicationContext(), SettlementActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("CID", c_id);
                    bundle.putString("SID", sid);
                    bundle.putString("NAME", select_customer.getText().toString().trim());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 22 && resultCode == RESULT_OK) {
            c_id = data.getExtras().getString("ID");
            sid = "";
            select_service.setText("");
            select_customer.setText(data.getExtras().getString("NAME"));
        }
        if (requestCode == 23 && resultCode == RESULT_OK) {
            sid = data.getExtras().getString("ID");
            select_service.setText(data.getExtras().getString("NAME"));
        }
    }
}