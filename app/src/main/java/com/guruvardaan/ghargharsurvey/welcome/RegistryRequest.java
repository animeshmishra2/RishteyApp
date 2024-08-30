package com.guruvardaan.ghargharsurvey.welcome;

import static com.guruvardaan.ghargharsurvey.config.Config.ADD_CUSTOMERS;
import static com.guruvardaan.ghargharsurvey.config.Config.GET_CUSTOMER_URL;
import static com.guruvardaan.ghargharsurvey.config.Config.GET_REG_AMOUNT;
import static com.guruvardaan.ghargharsurvey.config.Config.REQUEST_REGISTRY;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.utils.ProgressDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegistryRequest extends BaseActivity {

    EditText customer, service_name, customer_name, reg_date_1, reg_date_2, reg_date_3, registry_amount, remark, pay_mode;
    CardView submit;
    ImageView back_bt;
    String cust_id = "";
    String ser_id = "";
    ProgressDialog pd;
    UserDetails userDetails;
    String[] payment_array = new String[]{"Cash", "Cheque", "Bank Transfer", "Online"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_registry_request, frameLayout);
        customer = (EditText) findViewById(R.id.customer);
        userDetails = new UserDetails(getApplicationContext());
        ser_id = "";
        cust_id = "";
        service_name = (EditText) findViewById(R.id.service_name);
        pay_mode = (EditText) findViewById(R.id.pay_mode);
        customer_name = (EditText) findViewById(R.id.customer_name);
        reg_date_1 = (EditText) findViewById(R.id.reg_date_1);
        reg_date_2 = (EditText) findViewById(R.id.reg_date_2);
        reg_date_3 = (EditText) findViewById(R.id.reg_date_3);
        registry_amount = (EditText) findViewById(R.id.registry_amount);
        remark = (EditText) findViewById(R.id.remark);
        submit = (CardView) findViewById(R.id.submit);
        back_bt = (ImageView) findViewById(R.id.back_bt);
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customer.getText().toString().trim().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Select Customer", Toast.LENGTH_SHORT).show();
                } else if (service_name.getText().toString().trim().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Select Service", Toast.LENGTH_SHORT).show();
                } else if (customer_name.getText().toString().trim().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Enter Plot Owner Name", Toast.LENGTH_SHORT).show();
                } else if (reg_date_1.getText().toString().trim().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Select Registry Date 1", Toast.LENGTH_SHORT).show();
                } else if (reg_date_2.getText().toString().trim().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Select Registry Date 2", Toast.LENGTH_SHORT).show();
                } else if (reg_date_3.getText().toString().trim().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Select Registry Date 3", Toast.LENGTH_SHORT).show();
                } else if (registry_amount.getText().toString().trim().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Select Registry Amount", Toast.LENGTH_SHORT).show();
                } else if (pay_mode.getText().toString().trim().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Select Payment Mode", Toast.LENGTH_SHORT).show();
                } else if (remark.getText().toString().trim().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Enter Remark", Toast.LENGTH_SHORT).show();
                } else {
                    createJSON();
                }
            }
        });
        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectCustomerActivity.class);
                startActivityForResult(intent, 23);
            }
        });

        service_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cust_id.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Select Customer First", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), SelectServiceActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("SID", cust_id);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 24);
                }
            }
        });
        reg_date_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(RegistryRequest.this, R.style.MySpinnerDatePickerStyle,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                reg_date_1.setText(getForDate(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year));
                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                dpd.show();
            }
        });
        reg_date_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(RegistryRequest.this, R.style.MySpinnerDatePickerStyle,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                reg_date_2.setText(getForDate(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year));
                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                dpd.show();
            }
        });

        reg_date_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(RegistryRequest.this, R.style.MySpinnerDatePickerStyle,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                reg_date_3.setText(getForDate(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year));
                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                dpd.show();
            }
        });

        pay_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(RegistryRequest.this);
                mBuilder.setTitle("Select Payment Mode");
                mBuilder.setSingleChoiceItems(payment_array, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int s) {
                        pay_mode.setText(payment_array[s]);
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

    }

    public void createJSON() {
        try {
            JSONObject main_obj = new JSONObject();
            main_obj.put("key", "K9154289-68a1-80c7-e009-2asdccf7b0d");
            main_obj.put("advisorId", userDetails.getuserid());
            main_obj.put("memberId", cust_id);
            main_obj.put("serviceId", ser_id);
            main_obj.put("customername", customer_name.getText().toString().trim());
            main_obj.put("requestDt1", reg_date_1.getText().toString().trim());
            main_obj.put("requestDt2", reg_date_2.getText().toString().trim());
            main_obj.put("requestDt3", reg_date_3.getText().toString().trim());
            main_obj.put("registryamount", registry_amount.getText().toString().trim());
            main_obj.put("remark", remark.getText().toString().trim());
            main_obj.put("paymentmode", pay_mode.getText().toString().trim());
            System.out.println("Animesh " + main_obj.toString());
            addRequest(main_obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public String getForDate(String tt) {
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
            Date date = format1.parse(tt);
            return format2.format(date);
        } catch (Exception e) {
            return tt;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 23 && resultCode == RESULT_OK) {
            customer.setText(data.getExtras().getString("NAME"));
            cust_id = data.getExtras().getString("ID");
            service_name.setText("");
            ser_id = "";
        }
        if (requestCode == 24 && resultCode == RESULT_OK) {
            service_name.setText(data.getExtras().getString("NAME"));
            ser_id = data.getExtras().getString("ID");
            getRegistryAmount();
        }
        if (requestCode == 39 && resultCode == RESULT_OK) {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }

    private void getRegistryAmount() {
        pd = ProgressDialog.show(RegistryRequest.this, "Please Wait");
        String URL_CHECK = GET_REG_AMOUNT + "K9154289-68a1-80c7-e009-2asdccf7b0d/" + ser_id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray Table = jsonObject.getJSONArray("Table");
                            if (Table.length() > 0) {
                                JSONObject object = Table.getJSONObject(0);
                                registry_amount.setText(object.getString("registeryAmount"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        error.printStackTrace();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content_Type", "application/json");
                params.put("Accept", "application/json");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    public void addRequest(String jsons) {
        pd = ProgressDialog.show(RegistryRequest.this);
        StringRequest myReq = new StringRequest(Request.Method.POST,
                REQUEST_REGISTRY,
                createMyReqSuccessListener(),
                createMyReqErrorListener()) {

            @Override
            public byte[] getBody() throws com.android.volley.AuthFailureError {
                return jsons.getBytes();
            }

            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(myReq);
    }

    private Response.Listener<String> createMyReqSuccessListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                System.out.println(response);
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray Table = object.getJSONArray("Table");
                    if (Table.length() > 0) {
                        JSONObject objects = Table.getJSONObject(0);
                        if (!objects.has("return_type")) {
                            if (objects.getString("Column1").equalsIgnoreCase("t")) {
                                Intent intent = new Intent(getApplicationContext(), UploadRegistry.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("ID", objects.getString("id"));
                                intent.putExtras(bundle);
                                startActivityForResult(intent, 39);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), objects.getString("errormsg"), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Mishras " + response);
            }
        };
    }


    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), "Request Failed", Toast.LENGTH_SHORT).show();
            }
        };
    }

}