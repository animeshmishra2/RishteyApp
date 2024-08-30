package com.guruvardaan.ghargharsurvey.agent;

import static com.guruvardaan.ghargharsurvey.config.Config.CAR_MODEL_URL;
import static com.guruvardaan.ghargharsurvey.config.Config.PASSBOOK_TYPE_URL;

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
import com.guruvardaan.ghargharsurvey.model.PassbookTypeModel;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PassbookTypeActivity extends BaseActivity {

    RecyclerView passbook_recycler;
    UserDetails userDetails;
    ProgressBar progress;
    ImageView back_button;
    ArrayList<PassbookTypeModel> passbookTypeModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_passbook_type, frameLayout);
        back_button = (ImageView) findViewById(R.id.back_button);
        userDetails = new UserDetails(getApplicationContext());
        progress = (ProgressBar) findViewById(R.id.progress);
        passbook_recycler = (RecyclerView) findViewById(R.id.passbook_recycler);
        passbook_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        passbookTypeModels = new ArrayList<>();
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getPassbookType();
    }

    private void getPassbookType() {
        progress.setVisibility(View.VISIBLE);
        String URL_CHECK = PASSBOOK_TYPE_URL + "K9154289-68a1-80c7-e009-2asdccf7b0d/";
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
                                passbookTypeModels = new ArrayList<>();
                                passbookTypeModels.add(new PassbookTypeModel("all", "All"));
                                for (int i = 0; i < Table.length(); i++) {
                                    JSONObject object = Table.getJSONObject(i);
                                    passbookTypeModels.add(new PassbookTypeModel(object.getString("typeId"), object.getString("type_name")));
                                }
                                passbook_recycler.setAdapter(new PassbookTypeAdapter());
                            } else {
                                Toast.makeText(getApplicationContext(), "No Passbook Type Found", Toast.LENGTH_SHORT).show();

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

    class PassbookTypeAdapter extends RecyclerView.Adapter<PassbookTypeAdapter.TypeHolder> {

        @Override
        public int getItemCount() {
            return passbookTypeModels.size();

        }

        @Override
        public void onBindViewHolder(PassbookTypeAdapter.TypeHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.prod_name.setText(passbookTypeModels.get(position).getType_name());
            holder.pr_image.setImageResource(R.drawable.passbook);
            holder.prod_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent returnIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("NAME", passbookTypeModels.get(position).getType_name());
                    bundle.putString("ID", passbookTypeModels.get(position).getTypeId());
                    returnIntent.putExtras(bundle);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            });
        }

        @Override
        public PassbookTypeAdapter.TypeHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.vehicle_row, viewGroup, false);
            PassbookTypeAdapter.TypeHolder listHolder = new PassbookTypeAdapter.TypeHolder(mainGroup);
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