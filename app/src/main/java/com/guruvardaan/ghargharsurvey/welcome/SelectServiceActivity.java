package com.guruvardaan.ghargharsurvey.welcome;

import static com.guruvardaan.ghargharsurvey.config.Config.GET_CUSTOMER_URL;
import static com.guruvardaan.ghargharsurvey.config.Config.GET_SERVICE_URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.model.SelCusModel;
import com.guruvardaan.ghargharsurvey.model.ServiceModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectServiceActivity extends BaseActivity {
    RecyclerView passbook_recycler;
    UserDetails userDetails;
    ProgressBar progress;
    ImageView back_button;
    ArrayList<ServiceModel> serviceModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_select_service, frameLayout);
        back_button = (ImageView) findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        userDetails = new UserDetails(getApplicationContext());
        progress = (ProgressBar) findViewById(R.id.progress);
        passbook_recycler = (RecyclerView) findViewById(R.id.passbook_recycler);
        passbook_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        serviceModels = new ArrayList<>();
        getAllServices();
    }

    private void getAllServices() {
        progress.setVisibility(View.VISIBLE);
        String URL_CHECK = GET_SERVICE_URL + "K9154289-68a1-80c7-e009-2asdccf7b0d/" + getIntent().getExtras().getString("SID");
        System.out.println(URL_CHECK);
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
                                serviceModels = new ArrayList<>();
                                for (int i = 0; i < Table.length(); i++) {
                                    JSONObject object = Table.getJSONObject(i);
                                    if (object.has("name")) {
                                        serviceModels.add(new ServiceModel(object.getString("pk_acc_cor_service_id"), object.getString("name")));

                                    } else {
                                        serviceModels.add(new ServiceModel(object.getString("pk_acc_cor_service_id"), object.getString("pk_acc_cor_service_id")));
                                    }
                                }
                                passbook_recycler.setAdapter(new ServiceAdapter());
                            } else {
                                Toast.makeText(getApplicationContext(), "No Service Found", Toast.LENGTH_SHORT).show();

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

    class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.TypeHolder> {

        @Override
        public int getItemCount() {
            return serviceModels.size();

        }

        @Override
        public void onBindViewHolder(ServiceAdapter.TypeHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.prod_name.setText(serviceModels.get(position).getName());
            holder.pr_image.setImageResource(R.drawable.add_cust);
            holder.prod_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent returnIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("NAME", serviceModels.get(position).getName());
                    bundle.putString("ID", serviceModels.get(position).getPk_acc_cor_service_id());
                    returnIntent.putExtras(bundle);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            });
        }

        @Override
        public ServiceAdapter.TypeHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.vehicle_row, viewGroup, false);
            ServiceAdapter.TypeHolder listHolder = new ServiceAdapter.TypeHolder(mainGroup);
            return listHolder;
        }

        public class TypeHolder extends RecyclerView.ViewHolder {

            TextView prod_name;
            ImageView pr_image;
            LinearLayout prod_layout;

            public TypeHolder(View view) {
                super(view);
                prod_layout = (LinearLayout) view.findViewById(R.id.prod_layout);
                prod_name = (TextView) view.findViewById(R.id.prod_name);
                pr_image = (ImageView) view.findViewById(R.id.pr_image);
            }
        }
    }

}