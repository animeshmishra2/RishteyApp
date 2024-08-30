package com.guruvardaan.ghargharsurvey.customers;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;
import com.guruvardaan.ghargharsurvey.welcome.CustomerPassActivity;
import com.guruvardaan.ghargharsurvey.welcome.SelectCustomerActivity;
import com.guruvardaan.ghargharsurvey.welcome.SelectServiceActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PassbookCustmers extends BaseActivity {

    LinearLayout select_service_lay, select_start_lay;
    EditText select_service, select_start;
    TextView get_passbook;
    String sid = "";
    int mYear, mMonth, mDay;
    ImageView back_button;
    UserDetails userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.passbook_customer, frameLayout);
        userDetails = new UserDetails(getApplicationContext());
        back_button = (ImageView) findViewById(R.id.back_button);
        get_passbook = (TextView) findViewById(R.id.get_passbook);
        select_service_lay = (LinearLayout) findViewById(R.id.select_service_lay);
        select_start_lay = (LinearLayout) findViewById(R.id.select_start_lay);
        select_service = (EditText) findViewById(R.id.select_service);
        select_start = (EditText) findViewById(R.id.select_start);
        sid = "";

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
        select_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectServiceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("SID", userDetails.getuserid());
                intent.putExtras(bundle);
                startActivityForResult(intent, 23);
            }
        });
        select_start_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(PassbookCustmers.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                select_start.setText(getForDate(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year));
                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
                dpd.show();
            }
        });
        select_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(PassbookCustmers.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                select_start.setText(getForDate(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year));
                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
                dpd.show();
            }
        });

        get_passbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sid.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Select Service", Toast.LENGTH_SHORT).show();
                } else if (select_start.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Select Date", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), CustomerPassActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("SID", sid);
                    bundle.putString("CID", userDetails.getuserid());
                    bundle.putString("DATE", getForDateReturn(select_start.getText().toString().trim()));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
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

    public String getForDate(String tt) {
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat format2 = new SimpleDateFormat("dd MMM, yyyy");
            Date date = format1.parse(tt);
            return format2.format(date);
        } catch (Exception e) {
            return tt;
        }
    }

    public String getForDateReturn(String tt) {
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("dd MMM, yyyy");
            SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yyyy");
            Date date = format1.parse(tt);
            return format2.format(date);
        } catch (Exception e) {
            return tt;
        }
    }

}