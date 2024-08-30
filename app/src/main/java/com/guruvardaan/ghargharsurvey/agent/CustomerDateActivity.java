package com.guruvardaan.ghargharsurvey.agent;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;
import com.guruvardaan.ghargharsurvey.welcome.SelectCustomerActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CustomerDateActivity extends BaseActivity {

    ImageView back_button;
    EditText start_date, end_date, select_agent;
    TextView submit_date;
    String cust_id = "";
    UserDetails userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_customer_date, frameLayout);
        userDetails = new UserDetails(getApplicationContext());
        back_button = (ImageView) findViewById(R.id.back_button);
        start_date = (EditText) findViewById(R.id.start_date);
        end_date = (EditText) findViewById(R.id.end_date);
        select_agent = (EditText) findViewById(R.id.select_agent);
        submit_date = (TextView) findViewById(R.id.submit_date);
        cust_id = "";
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(CustomerDateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        start_date.setText(formatDate(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year));
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(CustomerDateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        end_date.setText(formatDate(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year));
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        select_agent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectCustomerActivity.class);
                startActivityForResult(intent, 23);
            }
        });
        submit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (start_date.getText().toString().trim().length() <= 0) {
                    Toast.makeText(CustomerDateActivity.this, "Select Start Date", Toast.LENGTH_SHORT).show();
                } else if (end_date.getText().toString().trim().length() <= 0) {
                    Toast.makeText(CustomerDateActivity.this, "Select End Date", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), ChequeStatusActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("POS", 0);
                    if (cust_id.equalsIgnoreCase("")) {
                        cust_id = "0";
                    }
                    bundle.putString("ST", start_date.getText().toString().trim());
                    bundle.putString("ED", end_date.getText().toString().trim());
                    bundle.putString("ID", cust_id);
                    bundle.putString("AID", userDetails.getuserid());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    public String formatDate(String dates) {
        try {
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            Date date = format.parse(dates);
            System.out.println(date);
            return format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return dates;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 23 && resultCode == RESULT_OK) {
            cust_id = data.getExtras().getString("ID");
            select_agent.setText(data.getExtras().getString("NAME"));
        }
    }
}