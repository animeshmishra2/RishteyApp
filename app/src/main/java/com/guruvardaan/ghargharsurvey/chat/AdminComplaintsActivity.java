package com.guruvardaan.ghargharsurvey.chat;

import static com.guruvardaan.ghargharsurvey.config.Config.ADMIN_COMPLAINTS;
import static com.guruvardaan.ghargharsurvey.config.Config.DASHBOARD;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.guruvardaan.ghargharsurvey.config.VolleySingleton;
import com.guruvardaan.ghargharsurvey.model.AdminCModel;
import com.guruvardaan.ghargharsurvey.model.NewChatModel;
import com.rishtey.agentapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdminComplaintsActivity extends AppCompatActivity {

    ProgressBar progress;
    ImageView back_button;
    RecyclerView recyclerView;
    ArrayList<AdminCModel> adminCModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_complaints);
        progress = (ProgressBar) findViewById(R.id.progress);
        adminCModels = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        back_button = (ImageView) findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getAdminComplaints();
    }

    private void getAdminComplaints() {
        progress.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ADMIN_COMPLAINTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        progress.setVisibility(View.GONE);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                                JSONArray data = jsonObject.getJSONArray("data");
                                adminCModels.clear();
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject object = data.getJSONObject(i);
                                    adminCModels.add(new AdminCModel(object.getString("DepartmentID"), object.getString("DepartmentName"), object.getString("DepartmentShortName"), object.getString("DepartmentCode"), object.getString("TotalComplaints"), object.getString("team_solved"), object.getString("total_solved"), object.getString("unsolved")));
                                }
                                if (data.length() > 0) {
                                    recyclerView.setVisibility(View.VISIBLE);
                                    recyclerView.setAdapter(new ComplaintAdapter());
                                } else {
                                    recyclerView.setVisibility(View.GONE);
                                }
                            } else {
                                recyclerView.setVisibility(View.GONE);
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
                        recyclerView.setVisibility(View.GONE);
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.MyViewHolder> {

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView department_name, total_complaints, next_complaints;
            LinearLayout header_lay;

            public MyViewHolder(View view) {
                super(view);
                total_complaints = (TextView) view.findViewById(R.id.total_complaints);
                next_complaints = (TextView) view.findViewById(R.id.next_complaints);
                department_name = (TextView) view.findViewById(R.id.department_name);
                header_lay = (LinearLayout) view.findViewById(R.id.header_lay);
            }
        }

        @Override
        public ComplaintAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.department_row, parent, false);
            return new ComplaintAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ComplaintAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.department_name.setText(adminCModels.get(position).getDepartmentName()+"("+adminCModels.get(position).getDepartmentShortName()+")");
            holder.total_complaints.setText("Total Complaints : "+adminCModels.get(position).getTotalComplaints());
            holder.next_complaints.setText("Solved : "+adminCModels.get(position).getTeam_solved()+"   Team Solved : "+adminCModels.get(position).getTotal_solved()+"   Unsolved : "+adminCModels.get(position).getUnsolved());
            holder.header_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return adminCModels.size();
        }
    }


}