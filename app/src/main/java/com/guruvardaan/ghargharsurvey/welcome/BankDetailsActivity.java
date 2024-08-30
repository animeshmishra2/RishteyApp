package com.guruvardaan.ghargharsurvey.welcome;

import static com.guruvardaan.ghargharsurvey.config.Config.GET_BANK_DETAILS;
import static com.guruvardaan.ghargharsurvey.config.Config.TODAY_BUSINESS;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.config.VolleySingleton;
import com.guruvardaan.ghargharsurvey.model.TodayBusinessModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BankDetailsActivity extends BaseActivity {

    TextView acc_number, b_name, ac_name, branch, ifsc, update_details;
    LinearLayout lin_lay;
    ProgressBar progress;
    UserDetails userDetails;
    String bid = "";
    ImageView back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_bank_details, frameLayout);
        bid = "";
        back_button = (ImageView) findViewById(R.id.back_button);
        userDetails = new UserDetails(getApplicationContext());
        acc_number = (TextView) findViewById(R.id.acc_number);
        b_name = (TextView) findViewById(R.id.b_name);
        progress = (ProgressBar) findViewById(R.id.progress);
        lin_lay = (LinearLayout) findViewById(R.id.lin_lay);
        ac_name = (TextView) findViewById(R.id.ac_name);
        branch = (TextView) findViewById(R.id.branch);
        ifsc = (TextView) findViewById(R.id.ifsc);
        update_details = (TextView) findViewById(R.id.update_details);
        getBankDetails();
        update_details.setVisibility(View.GONE);
        update_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UpdateDetails.class);
                Bundle bundle = new Bundle();
                bundle.putString("AC", acc_number.getText().toString().trim());
                bundle.putString("NM", ac_name.getText().toString().trim());
                bundle.putString("BN", b_name.getText().toString().trim());
                bundle.putString("BB", branch.getText().toString().trim());
                bundle.putString("IF", ifsc.getText().toString().trim());
                bundle.putString("ID", bid);
                intent.putExtras(bundle);
                startActivityForResult(intent, 23);
            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getBankDetails() {
        progress.setVisibility(View.VISIBLE);
        lin_lay.setVisibility(View.GONE);
        String URL_CHECK = GET_BANK_DETAILS + "K9154289-68a1-80c7-e009-2asdccf7b0d" + "/" + userDetails.getuserid();
        System.out.println(URL_CHECK);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.setVisibility(View.GONE);
                        lin_lay.setVisibility(View.VISIBLE);
                        System.out.println("Animesh " + response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray Table = jsonObject.getJSONArray("Table");
                            if (Table.length() > 0) {
                                JSONObject object = Table.getJSONObject(0);
                                ac_name.setText(object.getString("acHolder"));
                                acc_number.setText(object.getString("acNo"));
                                b_name.setText(object.getString("bank_name"));
                                branch.setText(object.getString("branch"));
                                ifsc.setText(object.getString("IFSC"));
                                bid = object.getString("bank");
                            }

                        } catch (Exception e) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.setVisibility(View.GONE);
                        lin_lay.setVisibility(View.VISIBLE);
                        error.printStackTrace();
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(BankDetailsActivity.this).addToRequestQueue(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 23 && resultCode == RESULT_OK) {
            getBankDetails();
        }
    }
}