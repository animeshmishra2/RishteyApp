package com.guruvardaan.ghargharsurvey.welcome;

import static com.guruvardaan.ghargharsurvey.config.Config.CHEQUE_STATUS_URL;
import static com.guruvardaan.ghargharsurvey.config.Config.LATEST_OFFER_URL;
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
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LatestOfferActivity extends BaseActivity {

    ImageView back_button;
    ProgressBar progress;
    TextView plot_name_old, size_old, plot_amount_old, paid_amount_old, balance_old, header_txt;
    TextView plot_name_new, plot_size_new, plot_amount_new, paid_amount_new, balance_new, offer_amount, note_txt, plot_date, amount_to_submit, amount_to_paid;
    LinearLayout no_plot_req;
    ScrollView settlement_scroll;
    TextView new_contract_txt;
    LinearLayout new_contract_lay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_latest_offer, frameLayout);
        back_button = (ImageView) findViewById(R.id.back_button);
        new_contract_lay = (LinearLayout) findViewById(R.id.new_contract_lay);
        new_contract_txt = (TextView) findViewById(R.id.new_contract_txt);
        settlement_scroll = (ScrollView) findViewById(R.id.settlement_scroll);
        amount_to_paid = (TextView) findViewById(R.id.amount_to_paid);
        amount_to_submit = (TextView) findViewById(R.id.amount_to_submit);
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
        plot_date = (TextView) findViewById(R.id.plot_date);
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, LATEST_OFFER_URL + getIntent().getExtras().getString("SID"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("ANIMESH " + response);
                        System.out.println("ANIMESH 2 " + LATEST_OFFER_URL + getIntent().getExtras().getString("SID"));
                        progress.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray Table = jsonObject.getJSONArray("Table");
                            if (Table.length() > 0) {
                                JSONObject object = Table.getJSONObject(0);
                                if (getDateCheck(object.getString("purchaseDate").trim())) {
                                    if (object.getString("plotName").trim().contains("A") || object.getString("plotName").trim().contains("B")) {
                                        new_contract_lay.setVisibility(View.GONE);
                                        new_contract_txt.setVisibility(View.GONE);
                                        note_txt.setText("1. यह ऑफर इस प्लाट पर नहीं लगाया जा सकता है , क्योंकि यह प्लाट A अथवा B ब्लॉक में है। \n2. यह ऑफर A अथवा B ब्लॉक के प्लाट पर नहीं उपलब्ध है। ");
                                    } else {
                                        if (getAmount(object.getString("plotArea").trim()) >= 1000) {
                                            if (get25Check(object.getString("currentTotalPlotAmount").trim(), object.getString("totalAMountWithoutOffer").trim())) {
                                                new_contract_lay.setVisibility(View.VISIBLE);
                                                new_contract_txt.setVisibility(View.VISIBLE);
                                                note_txt.setText("1. ₹" + object.getString("twentyfiveper") + " जमा करने पर  इस प्लाट का बचा हुआ पूरा पैसा EMI के हिसाब से हर महीने स्वयं ही पेड हो जायेगा\n2. अतः \u20b9 " + object.getString("twentyfiveper") + " जमा करने पर आपका बचा हुआ \u20b9 " + (getAmount(object.getString("newPlotAmount")) - (getAmount(object.getString("totalSubmittedAmount")) - getAmount(object.getString("offerAmount"))) - (getAmount(object.getString("twentyfiveper")))) + " ऑटो पेड हो जाएगा|");
                                            } else {
                                                new_contract_lay.setVisibility(View.GONE);
                                                new_contract_txt.setVisibility(View.GONE);
                                                note_txt.setText("1. यह ऑफर इस प्लाट पर नहीं लगाया जा सकता है , क्योंकि आपने इस प्लाट का 25 प्रतिशत पैसा जमा नहीं किया है।  25 प्रतिशत पैसा बिना ऑफर को शामिल किये माना जाएगा। \n2. यह ऑफर केवल उन्ही प्लाट के लिए वैध है जिनका 25 प्रतिशत पैसा बिना ऑफर लगाए हुए जमा है");
                                            }
                                        } else {
                                            new_contract_lay.setVisibility(View.GONE);
                                            new_contract_txt.setVisibility(View.GONE);
                                            note_txt.setText("1. यह ऑफर इस प्लाट पर नहीं लगाया जा सकता है , क्योंकि यह प्लाट 1000 स्क्वायर फ़ीट से कम है\n2. यह ऑफर केवल उन्ही प्लाट के लिए वैध है जिनका एरिया 1000 स्क्वायर फ़ीट के बराबर या उससे अधिक है। ");
                                        }
                                    }
                                } else {
                                    new_contract_lay.setVisibility(View.GONE);
                                    new_contract_txt.setVisibility(View.GONE);
                                    note_txt.setText("1. यह ऑफर इस प्लाट पर नहीं लगाया जा सकता है , क्योंकि यह प्लाट 1 जनवरी 2021 से पहले खरीदा गया है\n2. यह ऑफर केवल उन्ही प्लाट के लिए वैध है जिन्होंने प्लाट 1 जनवरी 2021 को या उसके बाद खरीदा है। ");
                                }
                                header_txt.setText(getIntent().getExtras().getString("NAME") + "'s " + object.getString("plotName"));
                                no_plot_req.setVisibility(View.GONE);
                                settlement_scroll.setVisibility(View.VISIBLE);
                                plot_name_new.setText(object.getString("plotName"));
                                plot_name_old.setText(object.getString("plotName"));
                                plot_date.setText(object.getString("purchaseDate"));
                                size_old.setText(object.getString("plotArea"));
                                plot_size_new.setText(object.getString("plotArea"));
                                plot_amount_old.setText(object.getString("currentTotalPlotAmount"));
                                plot_amount_new.setText(object.getString("newPlotAmount"));
                                paid_amount_old.setText(object.getString("totalSubmittedAmount"));
                                paid_amount_new.setText("" + (getAmount(object.getString("totalSubmittedAmount")) - getAmount(object.getString("offerAmount"))));
                                balance_old.setText(object.getString("currentBalanceAmount"));
                                balance_new.setText("" + (getAmount(object.getString("newPlotAmount")) - (getAmount(object.getString("totalSubmittedAmount")) - getAmount(object.getString("offerAmount")))));
                                offer_amount.setText(object.getString("offerAmount"));
                                amount_to_submit.setText(object.getString("twentyfiveper"));
                                amount_to_paid.setText("" + (getAmount(object.getString("newPlotAmount")) - (getAmount(object.getString("totalSubmittedAmount")) - getAmount(object.getString("offerAmount"))) - (getAmount(object.getString("twentyfiveper")))));
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


    public boolean getDateCheck(String purchaseDateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date purchaseDate = sdf.parse(purchaseDateString);
            Date comparisonDate = sdf.parse("31/12/2020");
            if (purchaseDate.after(comparisonDate)) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            return false;
        }
    }

    public boolean get25Check(String tot, String paid) {
        try {
            float t = Float.parseFloat(tot);
            float p = Float.parseFloat(paid);
            float t25 = (t * 25) / 100;
            if (p >= t25) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

}