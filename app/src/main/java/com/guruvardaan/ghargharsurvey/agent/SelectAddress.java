package com.guruvardaan.ghargharsurvey.agent;

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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.Config;
import com.guruvardaan.ghargharsurvey.model.AddressModel;
import com.guruvardaan.ghargharsurvey.model.SearchModel;
import com.guruvardaan.ghargharsurvey.visits.SelectCarModel;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SelectAddress extends BaseActivity {

    TextView header_txt;
    ImageView back_button;
    ArrayList<AddressModel> addressModels;
    RecyclerView recyclerView;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_select_address, frameLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        progress = (ProgressBar) findViewById(R.id.progress);
        header_txt = (TextView) findViewById(R.id.header_txt);
        back_button = (ImageView) findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        header_txt.setText(getIntent().getExtras().getString("TITLE"));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        getAddress();
    }

    public void getAddress() {
        recyclerView.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        String URL = "";
        if (getIntent().getExtras().getString("TITLE").equalsIgnoreCase("SELECT STATE")) {
            URL = Config.STATE_URL + "K9154289-68a1-80c7-e009-2asdccf7b0d/";
        } else if (getIntent().getExtras().getString("TITLE").equalsIgnoreCase("SELECT CITY")) {
            URL = Config.CITY_URL + "K9154289-68a1-80c7-e009-2asdccf7b0d/" + getIntent().getExtras().getString("ID");
        } else if (getIntent().getExtras().getString("TITLE").equalsIgnoreCase("SELECT TEHSIL")) {
            URL = Config.TEHSIL_URL + "K9154289-68a1-80c7-e009-2asdccf7b0d/" + getIntent().getExtras().getString("ID");
        } else if (getIntent().getExtras().getString("TITLE").equalsIgnoreCase("SELECT AREA")) {
            URL = Config.AREA_URL + "K9154289-68a1-80c7-e009-2asdccf7b0d/" + getIntent().getExtras().getString("ID");
        }
        StringRequest sr = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                recyclerView.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray Table = jsonObject.getJSONArray("Table");
                    addressModels = new ArrayList<>();
                    for (int i = 0; i < Table.length(); i++) {
                        JSONObject object = Table.getJSONObject(i);
                        if (getIntent().getExtras().getString("TITLE").equalsIgnoreCase("SELECT STATE")) {
                            addressModels.add(new AddressModel(object.getString("pk_utm_state_id"), object.getString("state_name")));
                        } else if (getIntent().getExtras().getString("TITLE").equalsIgnoreCase("SELECT CITY")) {
                            addressModels.add(new AddressModel(object.getString("pk_utm_city_id"), object.getString("city_name")));
                        } else if (getIntent().getExtras().getString("TITLE").equalsIgnoreCase("SELECT TEHSIL")) {
                            addressModels.add(new AddressModel(object.getString("id"), object.getString("tehsilName")));
                        } else if (getIntent().getExtras().getString("TITLE").equalsIgnoreCase("SELECT AREA")) {
                            addressModels.add(new AddressModel(object.getString("pk_utm_area_id"), object.getString("area_name")));
                        }
                    }
                    recyclerView.setAdapter(new AddressAdapter());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Response  " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                recyclerView.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
            }
        });
        sr.setRetryPolicy(new DefaultRetryPolicy(
                120000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);
    }

    class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.VehicleHolder> {

        @Override
        public int getItemCount() {
            return addressModels.size();

        }

        @Override
        public void onBindViewHolder(AddressAdapter.VehicleHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.prod_name.setText(addressModels.get(position).getName());
            holder.pr_image.setImageResource(R.drawable.track_location);
            holder.prod_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent returnIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("NAME", addressModels.get(position).getName());
                    bundle.putString("ID", addressModels.get(position).getId());
                    returnIntent.putExtras(bundle);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            });
        }

        @Override
        public AddressAdapter.VehicleHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.vehicle_row, viewGroup, false);
            AddressAdapter.VehicleHolder listHolder = new AddressAdapter.VehicleHolder(mainGroup);
            return listHolder;
        }

        public class VehicleHolder extends RecyclerView.ViewHolder {

            TextView prod_name;
            ImageView pr_image;
            LinearLayout prod_layout;

            public VehicleHolder(View view) {
                super(view);
                prod_layout = (LinearLayout) view.findViewById(R.id.prod_layout);
                prod_name = (TextView) view.findViewById(R.id.prod_name);
                pr_image = (ImageView) view.findViewById(R.id.pr_image);
            }
        }
    }

}