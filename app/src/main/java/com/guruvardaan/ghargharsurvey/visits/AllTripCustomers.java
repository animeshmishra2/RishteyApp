package com.guruvardaan.ghargharsurvey.visits;

import static com.guruvardaan.ghargharsurvey.config.Config.GET_ALL_REQUEST;
import static com.guruvardaan.ghargharsurvey.config.Config.GET_ALL_VISITS;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.guruvardaan.ghargharsurvey.model.PlotRequestModel;
import com.guruvardaan.ghargharsurvey.model.VisitModel;
import com.guruvardaan.ghargharsurvey.plots.PlotRequestForm;
import com.guruvardaan.ghargharsurvey.utils.ProgressDialog;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class AllTripCustomers extends BaseActivity {

    ImageView back_button;
    RecyclerView recyclerView;
    LinearLayout no_lay, plot_request;
    ArrayList<PlotRequestModel> plotRequestModels;
    UserDetails userDetails;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_all_trip_customers, frameLayout);
        no_lay = (LinearLayout) findViewById(R.id.no_lay);
        plot_request = (LinearLayout) findViewById(R.id.plot_request);
        userDetails = new UserDetails(getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        back_button = (ImageView) findViewById(R.id.back_button);
        plotRequestModels = new ArrayList<>();
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getAllRequest();

        plot_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PlotRequestForm.class);
                startActivityForResult(intent, 23);
            }
        });
    }

    private void getAllRequest() {
        no_lay.setVisibility(View.GONE);
        pd = ProgressDialog.show(AllTripCustomers.this);
        String URL_CHECK = GET_ALL_REQUEST + userDetails.getuserid();
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
                                no_lay.setVisibility(View.GONE);
                                plotRequestModels = new ArrayList<>();
                                for (int i = 0; i < Table.length(); i++) {
                                    JSONObject object = Table.getJSONObject(i);
                                    plotRequestModels.add(new PlotRequestModel(object.getString("id"), object.getString("agent_id"), object.getString("customer_name"), object.getString("customer_mobile"), object.getString("customer_address"), object.getString("professionId"), object.getString("professionName"), object.getString("occupationId"), object.getString("occupationName"), object.getString("status_of_plot_buy"), object.getString("payment_option"), object.getString("plot_size"), object.getString("block"), object.getString("category"), object.getString("emiforyear"), object.getString("visit_date"), object.getString("visit_amount"), object.getString("pan"), object.getString("aadhar_front"), object.getString("aadhar_back"), object.getString("meeting_pic")));
                                }
                                Collections.sort(plotRequestModels, new Comparator<PlotRequestModel>() {
                                    @Override
                                    public int compare(PlotRequestModel o1, PlotRequestModel o2) {
                                        return getIDs(o1.getId()) - getIDs(o2.getId());
                                    }
                                });
                                Collections.reverse(plotRequestModels);
                                recyclerView.setAdapter(new RequestAdapter());

                            } else {
                                no_lay.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(), "No Plot Request Found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            no_lay.setVisibility(View.VISIBLE);
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

    public int getIDs(String s) {
        try {
            int n = Integer.parseInt(s);
            return n;
        } catch (Exception e) {
            return 0;
        }
    }

    class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.VisitHolder> {

        @Override
        public int getItemCount() {
            return plotRequestModels.size();
        }

        @Override
        public void onBindViewHolder(RequestAdapter.VisitHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.user_name.setText(plotRequestModels.get(position).getCustomer_name());
            holder.user_mobile.setText(plotRequestModels.get(position).getCustomer_mobile());
            holder.block.setText(plotRequestModels.get(position).getBlock());
            holder.address.setText(plotRequestModels.get(position).getCustomer_address());
            holder.amount.setText("\u20b9 " + plotRequestModels.get(position).getVisit_amount());
            holder.status.setText(plotRequestModels.get(position).getStatus_of_plot_buy());
            holder.payment_mode.setText(plotRequestModels.get(position).getPayment_option());
            holder.visit_date.setText(plotRequestModels.get(position).getVisit_date());
        }

        @Override
        public RequestAdapter.VisitHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.plot_req_row, viewGroup, false);
            RequestAdapter.VisitHolder listHolder = new RequestAdapter.VisitHolder(mainGroup);
            return listHolder;
        }

        public class VisitHolder extends RecyclerView.ViewHolder {

            TextView user_name, user_mobile, block, address, amount, status, payment_mode, visit_date;

            public VisitHolder(View view) {
                super(view);
                user_name = (TextView) view.findViewById(R.id.user_name);
                user_mobile = (TextView) view.findViewById(R.id.user_mobile);
                block = (TextView) view.findViewById(R.id.block);
                address = (TextView) view.findViewById(R.id.address);
                amount = (TextView) view.findViewById(R.id.amount);
                status = (TextView) view.findViewById(R.id.status);
                payment_mode = (TextView) view.findViewById(R.id.payment_mode);
                visit_date = (TextView) view.findViewById(R.id.visit_date);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 23 && resultCode == RESULT_OK) {
            getAllRequest();
        }
    }
}