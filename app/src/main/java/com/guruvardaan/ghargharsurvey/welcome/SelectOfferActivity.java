package com.guruvardaan.ghargharsurvey.welcome;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.Config;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.model.OfferModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SelectOfferActivity extends BaseActivity {
    RecyclerView passbook_recycler;
    UserDetails userDetails;
    ProgressBar progress;
    ArrayList<OfferModel> offerModels;
    ImageView back_button;
    LinearLayout n_lay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_select_offer, frameLayout);
        userDetails = new UserDetails(getApplicationContext());
        progress = (ProgressBar) findViewById(R.id.progress);
        back_button = (ImageView) findViewById(R.id.back_button);
        n_lay = (LinearLayout) findViewById(R.id.n_lay);
        passbook_recycler = (RecyclerView) findViewById(R.id.passbook_recycler);
        passbook_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        offerModels = new ArrayList<>();
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getAllOffers();
    }

    private void getAllOffers() {
        progress.setVisibility(View.VISIBLE);
        n_lay.setVisibility(View.GONE);
        String URL_CHECK = Config.GET_OFFER_URL + getIntent().getExtras().getString("OFFER");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.setVisibility(View.GONE);
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray Table = jsonObject.getJSONArray("Table");
                            if (Table.length() > 0) {
                                offerModels = new ArrayList<>();
                                for (int i = 0; i < Table.length(); i++) {
                                    JSONObject object = Table.getJSONObject(i);
                                    offerModels.add(new OfferModel(object.getString("id"), object.getString("offerDetails"), object.getString("stDt"), object.getString("endDt"), object.getString("depositAmountFrom"), object.getString("depositAmountTo"), object.getString("offerAmount")));
                                }
                                passbook_recycler.setAdapter(new CustomerAdapter());
                            } else {
                                Toast.makeText(getApplicationContext(), "No Offers Found", Toast.LENGTH_SHORT).show();
                                n_lay.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            n_lay.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.setVisibility(View.GONE);
                        n_lay.setVisibility(View.VISIBLE);
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

    class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.TypeHolder> {

        @Override
        public int getItemCount() {
            return offerModels.size();
        }

        @Override
        public void onBindViewHolder(TypeHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.coupon_code.setText("\u20b9" + offerModels.get(position).getOfferAmount() + " extra");
            holder.description.setText("Deposit amount from \u20b9" + offerModels.get(position).getDepositAmountFrom() + " to \u20b9" + offerModels.get(position).getDepositAmountTo() + " to get extra of \u20b9" + offerModels.get(position).getOfferAmount());
            holder.apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.apply.getText().toString().trim().equalsIgnoreCase("Apply")) {
                        Intent returnIntent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("AMOUNT", offerModels.get(position).getOfferAmount());
                        bundle.putString("ID", offerModels.get(position).getId());
                        returnIntent.putExtras(bundle);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Offer has expired", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            if ((getMills(offerModels.get(position).getEndDt()) + 24 * 60 * 60 * 100) > System.currentTimeMillis()) {
                holder.apply.setText("Apply");
                holder.expiration.setText("Offer will expire on " + offerModels.get(position).getEndDt());
            } else {
                holder.expiration.setText("Offer has expired on " + offerModels.get(position).getEndDt());
                holder.apply.setText("Expired");
            }

        }

        @Override
        public TypeHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.coupons_row, viewGroup, false);
            TypeHolder listHolder = new TypeHolder(mainGroup);
            return listHolder;
        }

        public class TypeHolder extends RecyclerView.ViewHolder {
            TextView apply, description, expiration, coupon_code;


            public TypeHolder(View view) {
                super(view);
                coupon_code = (TextView) view.findViewById(R.id.coupon_code);
                apply = (TextView) view.findViewById(R.id.apply);
                description = (TextView) view.findViewById(R.id.description);
                expiration = (TextView) view.findViewById(R.id.expiration);
            }
        }
    }

    public long getMills(String s) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse(s);
            long millis = date.getTime();
            return millis;
        } catch (Exception e) {
            return 0;
        }

    }
}