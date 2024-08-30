package com.guruvardaan.ghargharsurvey.customers;

import static com.guruvardaan.ghargharsurvey.config.Config.GET_INSTALLMENT;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.agent.PayInstallmentActivity;
import com.guruvardaan.ghargharsurvey.config.VolleySingleton;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class InstallmentActivity extends BaseActivity {

    ImageView back_button;
    ProgressBar progress;
    LinearLayout main_layout;
    TextView total_service, total_paid_amount, total_late_fee, unbilled_amount, service_name, customer_name, total_due, pay_now, payable_amount, paid_amount, plot_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_installment, frameLayout);
        back_button = (ImageView) findViewById(R.id.back_button);
        progress = (ProgressBar) findViewById(R.id.progress);
        main_layout = (LinearLayout) findViewById(R.id.main_layout);
        plot_name = (TextView) findViewById(R.id.plot_name);
        total_service = (TextView) findViewById(R.id.total_service);
        total_paid_amount = (TextView) findViewById(R.id.total_paid_amount);
        total_late_fee = (TextView) findViewById(R.id.total_late_fee);
        unbilled_amount = (TextView) findViewById(R.id.unbilled_amount);
        service_name = (TextView) findViewById(R.id.service_name);
        customer_name = (TextView) findViewById(R.id.customer_name);
        total_due = (TextView) findViewById(R.id.total_due);
        pay_now = (TextView) findViewById(R.id.pay_now);
        payable_amount = (TextView) findViewById(R.id.payable_amount);
        paid_amount = (TextView) findViewById(R.id.paid_amount);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getAllInstallment();
        pay_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PayInstallmentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("SID", getIntent().getExtras().getString("SID"));
                bundle.putString("LF", total_late_fee.getText().toString().trim());
                bundle.putString("AM", total_due.getText().toString().trim());
                bundle.putString("CID", getIntent().getExtras().getString("CID"));
                bundle.putString("NAME", getIntent().getExtras().getString("NAME"));
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getAllInstallment() {
        main_layout.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        String URL_CHECK = GET_INSTALLMENT + "K9154289-68a1-80c7-e009-2asdccf7b0d" + "/" + getIntent().getExtras().getString("SID");
        System.out.println(URL_CHECK);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.setVisibility(View.GONE);
                        System.out.println(response);
                        main_layout.setVisibility(View.VISIBLE);
                        customer_name.setText(getIntent().getExtras().getString("NAME"));
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray Table = jsonObject.getJSONArray("Table");
                            if (Table.length() > 0) {
                                JSONObject object = Table.getJSONObject(0);
                                service_name.setText(object.getString("pk_acc_cor_service_id"));
                                total_service.setText(object.getString("totalAmount"));
                                total_due.setText(object.getString("currentPending"));
                                total_paid_amount.setText(object.getString("currenttotalPaid"));
                                paid_amount.setText(object.getString("currenttotalPaid"));
                                unbilled_amount.setText("" + (getData(object.getString("totalAmount")) - getData(object.getString("currenttotalAmount"))));
                                paid_amount.setText(object.getString("installment_amount"));
                                plot_name.setText(object.getString("plotName"));
                            }
                            JSONArray Table1 = jsonObject.getJSONArray("Table1");
                            if (Table1.length() > 0) {
                                JSONObject object = Table1.getJSONObject(0);
                                total_late_fee.setText(object.getString("late_fee"));
                                payable_amount.setText(object.getString("amount_left"));
                                unbilled_amount.setText(object.getString("amount_left"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.setVisibility(View.GONE);
                        error.printStackTrace();
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public float getData(String s) {
        try {
            float f = Float.parseFloat(s);
            return f;
        } catch (Exception e) {
            return 0;
        }
    }

}