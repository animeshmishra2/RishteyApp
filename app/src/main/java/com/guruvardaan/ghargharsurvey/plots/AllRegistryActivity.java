package com.guruvardaan.ghargharsurvey.plots;


import static com.guruvardaan.ghargharsurvey.config.Config.GET_REGISTRY_URL;

import android.annotation.SuppressLint;
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
import androidx.annotation.Nullable;
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
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.model.RegistryModel;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;
import com.guruvardaan.ghargharsurvey.welcome.RegistryRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllRegistryActivity extends BaseActivity {

    ProgressBar progress;
    RecyclerView registry_recycler;
    ImageView back_button;
    ArrayList<RegistryModel> registryModels;
    UserDetails userDetails;
    LinearLayout no_reg, request_registry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_all_registry, frameLayout);
        userDetails = new UserDetails(getApplicationContext());
        request_registry = (LinearLayout) findViewById(R.id.request_registry);
        registryModels = new ArrayList<>();
        no_reg = (LinearLayout) findViewById(R.id.no_reg);
        back_button = (ImageView) findViewById(R.id.back_button);
        progress = (ProgressBar) findViewById(R.id.progress);
        registry_recycler = (RecyclerView) findViewById(R.id.registry_recycler);
        registry_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getAllRegistry();
        request_registry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegistryRequest.class);
                startActivityForResult(intent, 22);
            }
        });
    }

    private void getAllRegistry() {
        progress.setVisibility(View.VISIBLE);
        no_reg.setVisibility(View.GONE);
        request_registry.setVisibility(View.GONE);
        registry_recycler.setVisibility(View.GONE);
        String URL_CHECK = GET_REGISTRY_URL + userDetails.getuserid() + "/";
        System.out.println("Mahendra " + URL_CHECK);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.setVisibility(View.GONE);
                        registry_recycler.setVisibility(View.VISIBLE);
                        request_registry.setVisibility(View.VISIBLE);
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray Table = jsonObject.getJSONArray("Table");
                            if (Table.length() > 0) {
                                registryModels = new ArrayList<>();
                                for (int i = 0; i < Table.length(); i++) {
                                    JSONObject object = Table.getJSONObject(i);
                                    registryModels.add(new RegistryModel(object.getString("id"), object.getString("advisorId"), object.getString("advisor_name"), object.getString("memberId"), object.getString("customerFirstName"), object.getString("dt"), object.getString("requestDt1"), object.getString("requestDt2"), object.getString("requestDt3"), object.getString("paymentMode"), object.getString("status"), object.getString("aadharFront"), object.getString("aadharBack"), object.getString("Pan"), object.getString("photo"), object.getString("cheque1"), object.getString("cheque2")));
                                }
                                registry_recycler.setAdapter(new RegistryAdapter());
                            } else {
                                no_reg.setVisibility(View.VISIBLE);
                                registry_recycler.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            no_reg.setVisibility(View.VISIBLE);
                            registry_recycler.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progress.setVisibility(View.GONE);
                        no_reg.setVisibility(View.VISIBLE);
                        registry_recycler.setVisibility(View.GONE);
                        request_registry.setVisibility(View.VISIBLE);
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

    class RegistryAdapter extends RecyclerView.Adapter<RegistryAdapter.RegistryHolder> {

        @Override
        public int getItemCount() {
            return registryModels.size();
        }

        @Override
        public void onBindViewHolder(RegistryAdapter.RegistryHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.user_name.setText(registryModels.get(position).getCustomerFirstName());
            holder.reg_dates.setText(registryModels.get(position).getRequestDt1() + ", " + registryModels.get(position).getRequestDt2() + ", " + registryModels.get(position).getRequestDt3());
            holder.req_date.setText(registryModels.get(position).getDt());
            holder.pay_mode.setText(registryModels.get(position).getPaymentMode());
            holder.status.setText(registryModels.get(position).getStatus());
        }

        @Override
        public RegistryAdapter.RegistryHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.registry_row, viewGroup, false);
            RegistryAdapter.RegistryHolder listHolder = new RegistryAdapter.RegistryHolder(mainGroup);
            return listHolder;
        }

        public class RegistryHolder extends RecyclerView.ViewHolder {

            TextView user_name, reg_dates, pay_mode, req_date, status;
            LinearLayout main_lay;

            public RegistryHolder(View view) {
                super(view);
                user_name = (TextView) view.findViewById(R.id.user_name);
                reg_dates = (TextView) view.findViewById(R.id.reg_dates);
                pay_mode = (TextView) view.findViewById(R.id.pay_mode);
                req_date = (TextView) view.findViewById(R.id.req_date);
                status = (TextView) view.findViewById(R.id.status);
                main_lay = (LinearLayout) view.findViewById(R.id.main_lay);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 22 && resultCode == RESULT_OK) {
            getAllRegistry();
        }
    }
}