package com.guruvardaan.ghargharsurvey.welcome;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.rishtey.agentapp.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CustomerPassbook extends BaseActivity {

    LinearLayout select_cust_lay, select_service_lay, select_start_lay;
    EditText select_customer, select_service, select_start;
    TextView get_passbook;
    String cid = "";
    String sid = "";
    int mYear, mMonth, mDay;
    ImageView back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_customer_passbook, frameLayout);
        back_button = (ImageView) findViewById(R.id.back_button);
        get_passbook = (TextView) findViewById(R.id.get_passbook);
        select_cust_lay = (LinearLayout) findViewById(R.id.select_cust_lay);
        select_service_lay = (LinearLayout) findViewById(R.id.select_service_lay);
        select_start_lay = (LinearLayout) findViewById(R.id.select_start_lay);
        select_customer = (EditText) findViewById(R.id.select_customer);
        select_service = (EditText) findViewById(R.id.select_service);
        select_start = (EditText) findViewById(R.id.select_start);
        cid = "";
        sid = "";

        select_cust_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectCustomerActivity.class);
                startActivityForResult(intent, 22);
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        select_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectCustomerActivity.class);
                startActivityForResult(intent, 22);
            }
        });

        select_service_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cid.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Select Customer", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), SelectServiceActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("SID", cid);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 23);
                }
            }
        });
        select_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cid.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Select Customer", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), SelectServiceActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("SID", cid);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 23);
                }
            }
        });
        select_start_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(CustomerPassbook.this,
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
                DatePickerDialog dpd = new DatePickerDialog(CustomerPassbook.this,
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
                if (cid.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Select Customer", Toast.LENGTH_SHORT).show();
                } else if (sid.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Select Service", Toast.LENGTH_SHORT).show();
                } else if (select_start.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Select Date", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), CustomerPassActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("SID", sid);
                    bundle.putString("CID", cid);
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
        if (requestCode == 22 && resultCode == RESULT_OK) {
            cid = data.getExtras().getString("ID");
            sid = "";
            select_service.setText("");
            select_customer.setText(data.getExtras().getString("NAME"));
        }
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