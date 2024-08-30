package com.guruvardaan.ghargharsurvey.welcome;

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
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.agent.TotalBusiness;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SelectDateActivity extends BaseActivity {

    EditText start_date, end_date;
    TextView submit_date;
    ImageView back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_select_date, frameLayout);
        back_button = (ImageView) findViewById(R.id.back_button);
        submit_date = (TextView) findViewById(R.id.submit_date);
        start_date = (EditText) findViewById(R.id.start_date);
        end_date = (EditText) findViewById(R.id.end_date);

        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(SelectDateActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(SelectDateActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                end_date.setText(formatDate(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year));
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        submit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (start_date.getText().toString().trim().length() <= 0) {
                    Toast.makeText(SelectDateActivity.this, "Select Start Date", Toast.LENGTH_SHORT).show();
                } else if (end_date.getText().toString().trim().length() <= 0) {
                    Toast.makeText(SelectDateActivity.this, "Select End Date", Toast.LENGTH_SHORT).show();
                } else {
                    if (getIntent().getExtras().getString("OPEN").equalsIgnoreCase("1")) {
                        Intent intent = new Intent(getApplicationContext(), TeamStatement.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("ST", start_date.getText().toString().trim());
                        bundle.putString("ED", end_date.getText().toString().trim());
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                    else if (getIntent().getExtras().getString("OPEN").equalsIgnoreCase("2")) {
                        Intent intent = new Intent(getApplicationContext(), TotalBusiness.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("POS", getIntent().getExtras().getInt("POS"));
                        bundle.putString("ST", start_date.getText().toString().trim());
                        bundle.putString("ED", end_date.getText().toString().trim());
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
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

}