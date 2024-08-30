package com.guruvardaan.ghargharsurvey.welcome;

import static com.guruvardaan.ghargharsurvey.config.Config.GET_BLOCK_URL;
import static com.guruvardaan.ghargharsurvey.config.Config.GET_PROPERTY_URL;

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
import com.guruvardaan.ghargharsurvey.config.UserDetails;
import com.guruvardaan.ghargharsurvey.model.PropertyModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectPropertyActivity extends BaseActivity {
    RecyclerView passbook_recycler;
    UserDetails userDetails;
    ProgressBar progress;
    ArrayList<PropertyModel> propertyModels;
    ImageView back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_select_property, frameLayout);
        back_button = (ImageView) findViewById(R.id.back_button);
        userDetails = new UserDetails(getApplicationContext());
        progress = (ProgressBar) findViewById(R.id.progress);
        passbook_recycler = (RecyclerView) findViewById(R.id.passbook_recycler);
        passbook_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        propertyModels = new ArrayList<>();
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getAllProperty();
    }

    private void getAllProperty() {
        progress.setVisibility(View.VISIBLE);
        passbook_recycler.setVisibility(View.GONE);
        String URL_CHECK = GET_PROPERTY_URL;
        System.out.println("Mahendra " + URL_CHECK);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.setVisibility(View.GONE);
                        passbook_recycler.setVisibility(View.VISIBLE);
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray Table = jsonObject.getJSONArray("Table");
                            if (Table.length() > 0) {
                                propertyModels = new ArrayList<>();
                                for (int i = 0; i < Table.length(); i++) {
                                    JSONObject object = Table.getJSONObject(i);
                                    propertyModels.add(new PropertyModel(object.getString("pk_prm_property_id"), object.getString("property_name")));
                                }
                                passbook_recycler.setAdapter(new PropertyAdapter());
                            } else {
                                Toast.makeText(getApplicationContext(), "No Properties Found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progress.setVisibility(View.GONE);
                        passbook_recycler.setVisibility(View.VISIBLE);
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

    class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.TypeHolder> {

        @Override
        public int getItemCount() {
            return propertyModels.size();

        }

        @Override
        public void onBindViewHolder(PropertyAdapter.TypeHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.prod_name.setText(propertyModels.get(position).getProperty_name());
            holder.pr_image.setImageResource(R.drawable.plots_av);
            holder.prod_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent returnIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("NAME", propertyModels.get(position).getProperty_name());
                    bundle.putString("ID", propertyModels.get(position).getPk_prm_property_id());
                    returnIntent.putExtras(bundle);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            });
        }

        @Override
        public PropertyAdapter.TypeHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.vehicle_row, viewGroup, false);
            PropertyAdapter.TypeHolder listHolder = new PropertyAdapter.TypeHolder(mainGroup);
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