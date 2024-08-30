package com.guruvardaan.ghargharsurvey.plots;

import static com.guruvardaan.ghargharsurvey.config.Config.GET_PROPERTY_URL;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
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
import com.guruvardaan.ghargharsurvey.model.PropertyModel;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OurPropertiesActivity extends BaseActivity {

    ProgressBar progress;
    RecyclerView property_recycler;
    ArrayList<PropertyModel> propertyModels;
    ImageView back_button;
    Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_our_properties, frameLayout);
        anim = new AlphaAnimation(0.4f, 1.0f);
        anim.setDuration(100); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        back_button = (ImageView) findViewById(R.id.back_button);
        property_recycler = (RecyclerView) findViewById(R.id.property_recycler);
        progress = (ProgressBar) findViewById(R.id.progress);
        property_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
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
        property_recycler.setVisibility(View.GONE);
        String URL_CHECK = GET_PROPERTY_URL;
        System.out.println("Mahendra " + URL_CHECK);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.setVisibility(View.GONE);
                        property_recycler.setVisibility(View.VISIBLE);
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
                                property_recycler.setAdapter(new PropertyAdapter());
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
                        property_recycler.setVisibility(View.VISIBLE);
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


    class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyHolder> {

        @Override
        public int getItemCount() {
            return propertyModels.size();
        }

        @Override
        public void onBindViewHolder(PropertyAdapter.PropertyHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.property_name.setText(propertyModels.get(position).getProperty_name());
            holder.property_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), BlockActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("PR", propertyModels.get(position).getPk_prm_property_id());
                    bundle.putString("PRO", propertyModels.get(position).getProperty_name());
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 29);
                }
            });

            holder.click_txt.startAnimation(anim);
        }

        @Override
        public PropertyAdapter.PropertyHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.property_row, viewGroup, false);
            PropertyAdapter.PropertyHolder listHolder = new PropertyAdapter.PropertyHolder(mainGroup);
            return listHolder;
        }

        public class PropertyHolder extends RecyclerView.ViewHolder {

            TextView property_name,click_txt;
            CardView property_card;

            public PropertyHolder(View view) {
                super(view);
                property_name = (TextView) view.findViewById(R.id.property_name);
                click_txt = (TextView) view.findViewById(R.id.click_txt);
                property_card = (CardView) view.findViewById(R.id.property_card);
            }
        }
    }
}