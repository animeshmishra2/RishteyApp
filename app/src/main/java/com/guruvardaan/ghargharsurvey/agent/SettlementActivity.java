package com.guruvardaan.ghargharsurvey.agent;

import static com.guruvardaan.ghargharsurvey.config.Config.CHEQUE_STATUS_URL;
import static com.guruvardaan.ghargharsurvey.config.Config.SETTLEMENT_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SettlementActivity extends BaseActivity {

    ImageView back_button;
    ProgressBar progress;
    TextView plot_name_old, size_old, plot_amount_old, paid_amount_old, balance_old, header_txt;
    TextView plot_name_new, plot_size_new, plot_amount_new, paid_amount_new, balance_new, offer_amount, note_txt;
    LinearLayout no_plot_req;
    ScrollView settlement_scroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_settlement, frameLayout);
        back_button = (ImageView) findViewById(R.id.back_button);
        settlement_scroll = (ScrollView) findViewById(R.id.settlement_scroll);
        no_plot_req = (LinearLayout) findViewById(R.id.no_plot_req);
        progress = (ProgressBar) findViewById(R.id.progress);
        header_txt = (TextView) findViewById(R.id.header_txt);
        plot_name_old = (TextView) findViewById(R.id.plot_name_old);
        size_old = (TextView) findViewById(R.id.size_old);
        offer_amount = (TextView) findViewById(R.id.offer_amount);
        note_txt = (TextView) findViewById(R.id.note_txt);
        plot_amount_old = (TextView) findViewById(R.id.plot_amount_old);
        paid_amount_old = (TextView) findViewById(R.id.paid_amount_old);
        balance_old = (TextView) findViewById(R.id.balance_old);
        plot_name_new = (TextView) findViewById(R.id.plot_name_new);
        plot_size_new = (TextView) findViewById(R.id.plot_size_new);
        plot_amount_new = (TextView) findViewById(R.id.plot_amount_new);
        paid_amount_new = (TextView) findViewById(R.id.paid_amount_new);
        balance_new = (TextView) findViewById(R.id.balance_new);
        header_txt.setText(getIntent().getExtras().getString("NAME"));
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getSettlementAmount();
    }

    private void getSettlementAmount() {
        progress.setVisibility(View.VISIBLE);
        no_plot_req.setVisibility(View.GONE);
        settlement_scroll.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, SETTLEMENT_URL + getIntent().getExtras().getString("SID"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        progress.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray Table = jsonObject.getJSONArray("Table");
                            if (Table.length() > 0) {
                                JSONObject object = Table.getJSONObject(0);
                                header_txt.setText(getIntent().getExtras().getString("NAME") + "'s " + object.getString("plotName"));
                                no_plot_req.setVisibility(View.GONE);
                                settlement_scroll.setVisibility(View.VISIBLE);
                                plot_name_new.setText(object.getString("plotName"));
                                plot_name_old.setText(object.getString("plotName"));
                                size_old.setText(object.getString("plotArea"));
                                plot_size_new.setText(object.getString("plotArea"));
                                plot_amount_old.setText(object.getString("currentTotalPlotAmount"));
                                plot_amount_new.setText(object.getString("newPlotAmount"));
                                paid_amount_old.setText(object.getString("totalSubmittedAmount"));
                                paid_amount_new.setText("" + (getAmount(object.getString("totalSubmittedAmount")) - getAmount(object.getString("offerAmount"))));
                                balance_old.setText(object.getString("currentBalanceAmount"));
                                balance_new.setText(object.getString("newBalanceAmount"));
                                offer_amount.setText(object.getString("offerAmount"));
                                note_txt.setText("1. Balance Amount ₹" + object.getString("newBalanceAmount") + " जमा करने पर  A ब्लॉक में " + object.getString("plotArea") + " स्क्वायर फ़ीट कार्नर का एक और प्लाट मिल जाएगा वो भी बिलकुल फ्री" + "\n2. आपका फायदा " + object.getString("plotArea") + " + " + object.getString("plotArea") + " स्क्वायर फ़ीट प्लाट अमाउंट \u20b9 " + object.getString("newPlotAmount") + " तो प्लाट वैल्यू \u20b9" + (getAmount(object.getString("newPlotAmount")) / (2 * getAmount(object.getString("plotArea")))) + " / स्क्वायर फ़ीट");
                            } else {
                                no_plot_req.setVisibility(View.VISIBLE);
                                settlement_scroll.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            no_plot_req.setVisibility(View.VISIBLE);
                            settlement_scroll.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        no_plot_req.setVisibility(View.VISIBLE);
                        settlement_scroll.setVisibility(View.GONE);
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

    public float getAmount(String ss) {
        try {
            float f = Float.parseFloat(ss);
            return f;
        } catch (Exception e) {
            return 0;
        }
    }

}