package com.guruvardaan.ghargharsurvey.agent;

import static com.guruvardaan.ghargharsurvey.config.Config.CHEQUE_STATUS_URL;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.guruvardaan.ghargharsurvey.model.ChequeStatusModel;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChequeStatusActivity extends BaseActivity {

    LinearLayout no_plot_req;
    ProgressBar progress;
    UserDetails userDetails;
    RecyclerView recyclerView;
    ImageView back_button;
    ArrayList<ChequeStatusModel> chequeStatusModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_cheque_status, frameLayout);
        back_button = (ImageView) findViewById(R.id.back_button);
        userDetails = new UserDetails(getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        no_plot_req = (LinearLayout) findViewById(R.id.no_plot_req);
        progress = (ProgressBar) findViewById(R.id.progress);
        chequeStatusModels = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getAllChequeStatus();
    }

    private void getAllChequeStatus() {
        progress.setVisibility(View.VISIBLE);
        no_plot_req.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        String URL_CHECK = CHEQUE_STATUS_URL + getIntent().getExtras().getString("AID") + "/" + getIntent().getExtras().getString("ID") + "/" + getIntent().getExtras().getString("ST") + "/" + getIntent().getExtras().getString("ED");
        System.out.println(URL_CHECK);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray Table = jsonObject.getJSONArray("Table");
                            if (Table.length() > 0) {
                                chequeStatusModels.clear();
                                for (int i = 0; i < Table.length(); i++) {
                                    JSONObject object = Table.getJSONObject(i);
                                    chequeStatusModels.add(new ChequeStatusModel(object.getString("id"), object.getString("chequeNo"), object.getString("bank_name"), object.getString("branch"), object.getString("chequeDt"), object.getString("amount"), object.getString("cid"), object.getString("customerFirstName"), object.getString("advisorId"), object.getString("advisor_name"), object.getString("userComment"), object.getString("empComment"), object.getString("empins_date"), object.getString("empins_time"), object.getString("username"), object.getString("chequeImage"), object.getString("paymentSlip"), object.getString("ins_date"), object.getString("ChequeStatus")));
                                }
                                recyclerView.setAdapter(new StatusAdapter());
                            } else {
                                no_plot_req.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            no_plot_req.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.setVisibility(View.GONE);
                        no_plot_req.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
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

    class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.MyViewHolder> {

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView user_name, cheque_number, cheque_amount, cheque_date, customer_id, bank_name, advisor_name, cheque_status, ins_date, remark;

            public MyViewHolder(View view) {
                super(view);
                user_name = (TextView) view.findViewById(R.id.user_name);
                cheque_number = (TextView) view.findViewById(R.id.cheque_number);
                cheque_amount = (TextView) view.findViewById(R.id.cheque_amount);
                cheque_date = (TextView) view.findViewById(R.id.cheque_date);
                customer_id = (TextView) view.findViewById(R.id.customer_id);
                bank_name = (TextView) view.findViewById(R.id.bank_name);
                advisor_name = (TextView) view.findViewById(R.id.advisor_name);
                cheque_status = (TextView) view.findViewById(R.id.cheque_status);
                ins_date = (TextView) view.findViewById(R.id.ins_date);
                remark = (TextView) view.findViewById(R.id.remark);
            }
        }

        @Override
        public StatusAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cheque_status_row, parent, false);
            return new StatusAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(StatusAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.user_name.setText(chequeStatusModels.get(position).getCustomerFirstName());
            holder.cheque_number.setText(chequeStatusModels.get(position).getChequeNo());
            holder.cheque_amount.setText("\u20b9" + chequeStatusModels.get(position).getAmount());
            holder.cheque_date.setText(chequeStatusModels.get(position).getChequeDt());
            holder.customer_id.setText(chequeStatusModels.get(position).getCid());
            holder.bank_name.setText(chequeStatusModels.get(position).getBank_name() + " " + chequeStatusModels.get(position).getBranch());
            holder.advisor_name.setText(chequeStatusModels.get(position).getAdvisor_name() + " (" + chequeStatusModels.get(position).getAdvisorId() + ")");
            holder.cheque_status.setText(chequeStatusModels.get(position).getChequeStatus());
            holder.ins_date.setText(chequeStatusModels.get(position).getIns_date());
            holder.remark.setText(chequeStatusModels.get(position).getEmpComment() + " by " + chequeStatusModels.get(position).getUsername());
            if (chequeStatusModels.get(position).getChequeStatus().equalsIgnoreCase("Approved")) {
                holder.cheque_status.setTextColor(Color.parseColor("#2f9c00"));
                holder.remark.setText(chequeStatusModels.get(position).getEmpComment() + " by " + chequeStatusModels.get(position).getUsername());
            } else if (chequeStatusModels.get(position).getChequeStatus().equalsIgnoreCase("Rejected")) {
                holder.cheque_status.setTextColor(Color.parseColor("#ff0000"));
                holder.remark.setText(chequeStatusModels.get(position).getEmpComment() + " by " + chequeStatusModels.get(position).getUsername());
            } else {
                holder.remark.setText("Not Processed Yet");
                holder.cheque_status.setTextColor(Color.parseColor("#f4b800"));
            }
        }

        @Override
        public int getItemCount() {
            return chequeStatusModels.size();
        }

    }

}