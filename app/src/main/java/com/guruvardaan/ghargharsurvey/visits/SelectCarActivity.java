package com.guruvardaan.ghargharsurvey.visits;

import static com.guruvardaan.ghargharsurvey.config.Config.GET_CARS_URL;

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
import com.guruvardaan.ghargharsurvey.model.AgentCarModel;
import com.guruvardaan.ghargharsurvey.utils.ProgressDialog;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectCarActivity extends BaseActivity {

    RecyclerView type_recyclerview;
    ArrayList<AgentCarModel> agentCarModels;
    ProgressDialog pd;
    UserDetails userDetails;
    LinearLayout add_new_car;
    ImageView back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_select_car, frameLayout);
        back_button = (ImageView) findViewById(R.id.back_button);
        userDetails = new UserDetails(getApplicationContext());
        type_recyclerview = (RecyclerView) findViewById(R.id.type_recyclerview);
        type_recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        agentCarModels = new ArrayList<>();
        add_new_car = (LinearLayout) findViewById(R.id.add_new_car);
        getCarModels();
        add_new_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddCarActivity.class);
                startActivityForResult(intent, 23);
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleHolder> {

        @Override
        public int getItemCount() {
            return agentCarModels.size();

        }

        @Override
        public void onBindViewHolder(VehicleHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.car_model.setText(agentCarModels.get(position).getCarName());
            holder.car_number.setText(agentCarModels.get(position).getCarNumber());
            holder.car_status.setText(agentCarModels.get(position).getStatus());
            if (agentCarModels.get(position).getStatus().equalsIgnoreCase("Admin Approved")) {
                holder.car_layout.setAlpha(1.0f);
            } else {
                holder.car_layout.setAlpha(0.5f);
            }
            holder.car_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (agentCarModels.get(position).getStatus().equalsIgnoreCase("Admin Approved")) {
                        Intent returnIntent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("NAME", agentCarModels.get(position).getCarNumber());
                        bundle.putString("ID", agentCarModels.get(position).getSelected());
                        returnIntent.putExtras(bundle);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "This Car is not Approved by Admin", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public VehicleHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.agent_car_row, viewGroup, false);
            VehicleHolder listHolder = new VehicleHolder(mainGroup);
            return listHolder;
        }

        public class VehicleHolder extends RecyclerView.ViewHolder {
            TextView car_number, car_model, car_status;
            LinearLayout car_layout;

            public VehicleHolder(View view) {
                super(view);
                car_layout = (LinearLayout) view.findViewById(R.id.car_layout);
                car_number = (TextView) view.findViewById(R.id.car_number);
                car_model = (TextView) view.findViewById(R.id.car_model);
                car_status = (TextView) view.findViewById(R.id.car_status);
            }
        }
    }


    private void getCarModels() {
        pd = ProgressDialog.show(SelectCarActivity.this);
        String URL_CHECK = GET_CARS_URL + "K9154289-68a1-80c7-e009-2asdccf7b0d/" + userDetails.getuserid();
        System.out.println("Mahendra " + URL_CHECK);
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
                                agentCarModels = new ArrayList<>();
                                for (int i = 0; i < Table.length(); i++) {
                                    JSONObject object = Table.getJSONObject(i);
                                    agentCarModels.add(new AgentCarModel(object.getString("carName"), object.getString("carNumber"), object.getString("rcImage"), object.getString("loanDocument1"), object.getString("loanDocument2"), object.getString("loanDocument3"), object.getString("status"), object.getString("carDocumentID")));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 23 && resultCode == RESULT_OK) {
            getCarModels();
        }
    }
}