package com.guruvardaan.ghargharsurvey.chat;

import static com.guruvardaan.ghargharsurvey.config.Config.GET_COMPLAINT_DATA;
import static com.guruvardaan.ghargharsurvey.config.Config.GET_DEPARTMENT_USERS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rishtey.agentapp.R;
import com.guruvardaan.ghargharsurvey.config.VolleySingleton;
import com.guruvardaan.ghargharsurvey.model.EmployeeModel;
import com.guruvardaan.ghargharsurvey.model.ResModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TransferActivity extends AppCompatActivity {

    ProgressBar progressBar;
    RecyclerView recyclerView;
    ImageView goBack;
    ArrayList<EmployeeModel> employeeModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        goBack = (ImageView) findViewById(R.id.goBack);
        employeeModels = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getAllEmployee();
    }

    private void getAllEmployee() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        String URL_CHECK = GET_DEPARTMENT_USERS + "2";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        employeeModels.clear();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            employeeModels.add(new EmployeeModel(object.getString("id"), object.getString("EmpId"), object.getString("FirstName"), object.getString("AcName"), object.getString("EmailId"), object.getString("Password"), object.getString("Gender"), object.getString("Dob"), object.getString("Department"), object.getString("is_teamlead"), object.getString("team_lead"), object.getString("Phonenumber"), object.getString("Status"), object.getString("firebase_id"), object.getString("RegDate"), object.getString("DepartmentName")));
                        }
                        recyclerView.setAdapter(new TeamAdapter());
                    } else {
                        Toast.makeText(getApplicationContext(), "No Employee Found", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "No Employee Found", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.MyViewHolder> {

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView type_employee, user_email, department_name, transfer, mobile, user_name, status_txt;
            ImageView status_img;

            public MyViewHolder(View view) {
                super(view);
                type_employee = (TextView) view.findViewById(R.id.type_employee);
                user_email = (TextView) view.findViewById(R.id.user_email);
                department_name = (TextView) view.findViewById(R.id.department_name);
                status_img = (ImageView) view.findViewById(R.id.status_img);
                transfer = (TextView) view.findViewById(R.id.transfer);
                mobile = (TextView) view.findViewById(R.id.mobile);
                user_name = (TextView) view.findViewById(R.id.user_name);
                status_txt = (TextView) view.findViewById(R.id.status_txt);
            }
        }

        @Override
        public TeamAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_row, parent, false);
            return new TeamAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(TeamAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            if (employeeModels.get(position).getIs_teamlead().equalsIgnoreCase("1")) {
                holder.type_employee.setText("Team Lead");
            } else {
                holder.type_employee.setText("Employee");
            }
            holder.user_name.setText(employeeModels.get(position).getAcName());
            holder.user_email.setText(employeeModels.get(position).getEmailId());
            holder.mobile.setText(employeeModels.get(position).getPhonenumber());
            holder.department_name.setText(employeeModels.get(position).getDepartmentName());
            if (employeeModels.get(position).getStatus().equalsIgnoreCase("0")) {
                holder.status_img.setImageResource(R.drawable.rejected);
                holder.status_txt.setText("Inactive");
                holder.status_txt.setTextColor(Color.parseColor("#ff0000"));
            } else {
                holder.status_img.setImageResource(R.drawable.approved);
                holder.status_txt.setText("Active");
                holder.status_txt.setTextColor(Color.parseColor("#05c505"));
            }
            holder.transfer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent returnIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("ID", employeeModels.get(position).getId());
                    bundle.putString("NAME", employeeModels.get(position).getFirstName());
                    returnIntent.putExtras(bundle);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return employeeModels.size();
        }
    }

}