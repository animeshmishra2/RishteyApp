package com.guruvardaan.ghargharsurvey.visits;

import static com.guruvardaan.ghargharsurvey.config.Config.PROFESSION_URL;

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
import com.guruvardaan.ghargharsurvey.model.ProfessionModel;
import com.guruvardaan.ghargharsurvey.utils.ProgressDialog;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectProfession extends BaseActivity {

    RecyclerView type_recyclerview;
    ArrayList<ProfessionModel> professionModels;
    ProgressDialog pd;
    UserDetails userDetails;
    ImageView back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_profession, frameLayout);
        back_button = (ImageView) findViewById(R.id.back_button);
        userDetails = new UserDetails(getApplicationContext());
        type_recyclerview = (RecyclerView) findViewById(R.id.type_recyclerview);
        type_recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        professionModels = new ArrayList<>();
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getProfession();
    }

    class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleHolder> {

        @Override
        public int getItemCount() {
            return professionModels.size();

        }

        @Override
        public void onBindViewHolder(VehicleHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.prod_name.setText(professionModels.get(position).getOccupationName());
            holder.pr_image.setImageResource(R.drawable.add_supers);
            holder.prod_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent returnIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("NAME", professionModels.get(position).getOccupationName());
                    bundle.putString("ID", professionModels.get(position).getId());
                    returnIntent.putExtras(bundle);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            });
        }

        @Override
        public VehicleHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.vehicle_row, viewGroup, false);
            VehicleHolder listHolder = new VehicleHolder(mainGroup);
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


    private void getProfession() {
        pd = ProgressDialog.show(SelectProfession.this);
        String URL_CHECK = PROFESSION_URL + "K9154289-68a1-80c7-e009-2asdccf7b0d/1/";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray Table = jsonObject.getJSONArray("Table");
                            if (Table.length() > 0) {
                                professionModels = new ArrayList<>();
                                for (int i = 0; i < Table.length(); i++) {
                                    JSONObject object = Table.getJSONObject(i);
                                    professionModels.add(new ProfessionModel(object.getString("id"), object.getString("occupationName")));
                                }
                                type_recyclerview.setAdapter(new VehicleAdapter());
                            } else {
                                Toast.makeText(getApplicationContext(), "No Cars Found", Toast.LENGTH_SHORT).show();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
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

}