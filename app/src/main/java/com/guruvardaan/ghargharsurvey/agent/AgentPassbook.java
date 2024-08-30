package com.guruvardaan.ghargharsurvey.agent;

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
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AgentPassbook extends BaseActivity {

    LinearLayout select_type_lay, select_start_lay, select_end_lay;
    EditText select_type, select_start, select_end;
    TextView get_passbook;
    String id = "";
    int mYear, mMonth, mDay;
    ImageView back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_agent_passbook, frameLayout);
        id = "";
        back_button = (ImageView) findViewById(R.id.back_button);
        get_passbook = (TextView) findViewById(R.id.get_passbook);
        select_type = (EditText) findViewById(R.id.select_type);
        select_start = (EditText) findViewById(R.id.select_start);
        select_end = (EditText) findViewById(R.id.select_end);
        select_type_lay = (LinearLayout) findViewById(R.id.select_type_lay);
        select_start_lay = (LinearLayout) findViewById(R.id.select_start_lay);
        select_end_lay = (LinearLayout) findViewById(R.id.select_end_lay);

        select_type_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PassbookTypeActivity.class);
                startActivityForResult(intent, 22);
            }
        });
        select_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PassbookTypeActivity.class);
                startActivityForResult(intent, 22);
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        select_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(AgentPassbook.this,
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
        select_start_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(AgentPassbook.this,
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
        select_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(AgentPassbook.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                select_end.setText(getForDate(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year));
                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
                dpd.show();
            }
        });
        select_end_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(AgentPassbook.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                select_end.setText(getForDate(dayOfMonth + "-"
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
                if (id.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Select Passbook Type", Toast.LENGTH_SHORT).show();
                    return;
                } else if (select_start.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Select Start Date", Toast.LENGTH_SHORT).show();
                    return;
                } else if (select_end.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Select End Date", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Intent intent = new Intent(getApplicationContext(), AgentPassActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("ED", getForDateReturn(select_end.getText().toString().trim()));
                    bundle.putString("SD", getForDateReturn(select_start.getText().toString().trim()));
                    bundle.putString("TYPE", id);
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
            id = data.getExtras().getString("ID");
            select_type.setText(data.getExtras().getString("NAME"));
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