package com.guruvardaan.ghargharsurvey.agent;

import static com.guruvardaan.ghargharsurvey.config.Config.AGENT_ALL_PLOT_REQUEST;

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
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.guruvardaan.ghargharsurvey.customers.ViewRequestActivity;
import com.guruvardaan.ghargharsurvey.model.MyPlotRequestModel;
import com.guruvardaan.ghargharsurvey.welcome.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AllPlotRequestActivity extends BaseActivity {

    LinearLayout no_plot_req;
    ProgressBar progress;
    UserDetails userDetails;
    RecyclerView recyclerView;
    ImageView back_button;
    ArrayList<MyPlotRequestModel> myPlotRequestModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_my_plot_request, frameLayout);
        back_button = (ImageView) findViewById(R.id.back_button);
        userDetails = new UserDetails(getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        no_plot_req = (LinearLayout) findViewById(R.id.no_plot_req);
        progress = (ProgressBar) findViewById(R.id.progress);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        myPlotRequestModels = new ArrayList<>();
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getAllRequest();
    }

    private void getAllRequest() {
        progress.setVisibility(View.VISIBLE);
        no_plot_req.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        String URL_CHECK = AGENT_ALL_PLOT_REQUEST + userDetails.getuserid();
        System.out.println(URL_CHECK);
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
                                myPlotRequestModels = new ArrayList<>();
                                for (int i = 0; i < Table.length(); i++) {
                                    JSONObject object = Table.getJSONObject(i);
                                    myPlotRequestModels.add(new MyPlotRequestModel(object.getString("id"), object.getString("advisorId"), object.getString("uniqueName"), object.getString("userDetails"), object.getString("empComment"), object.getString("ins_date"), object.getString("requested_property"), object.getString("requested_block"), object.getString("property_type_name"), object.getString("requested_plot"), object.getString("advisor_name"), object.getString("customerName"), object.getString("booked_property"), object.getString("booked_block"), object.getString("amount"), object.getString("booked_plot"), object.getString("pk_prm_property_id"), object.getString("pk_prm_block_id"), object.getString("pk_prm_property_type"), object.getString("pk_prm_define_property_id"), object.getString("status"), object.getString("allotedBlockName"), object.getString("allotedPropertyName"), object.getString("allotedPlotName"), object.getString("allotedBlockId"), object.getString("request_plot_id"), object.getString("totalPayments"), object.getString("requestedCustomerId"), object.getString("RequestedcustomerName")));
                                }
                                Collections.sort(myPlotRequestModels, new Comparator<MyPlotRequestModel>() {
                                    @Override
                                    public int compare(MyPlotRequestModel p1, MyPlotRequestModel p2) {
                                        return getIds(p1.getId()) - getIds(p2.getId()); // Ascending
                                    }

                                });
                                Collections.reverse(myPlotRequestModels);
                                recyclerView.setVisibility(View.VISIBLE);
                                no_plot_req.setVisibility(View.GONE);
                                recyclerView.setAdapter(new RequestAdapter());
                            } else {
                                recyclerView.setVisibility(View.GONE);
                                no_plot_req.setVisibility(View.VISIBLE);
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
                        no_plot_req.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
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

    class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestHolder> {

        @Override
        public int getItemCount() {
            return myPlotRequestModels.size();
        }

        @Override
        public void onBindViewHolder(RequestHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.plot_name.setText("PLOT - " + myPlotRequestModels.get(position).getUniqueName());
            holder.your_rem.setText(myPlotRequestModels.get(position).getUserDetails());
            holder.emp_remark.setText(myPlotRequestModels.get(position).getEmpComment());
            holder.date_txt.setText(getForDate(myPlotRequestModels.get(position).getIns_date()));
            if (myPlotRequestModels.get(position).getEmpComment().trim().equalsIgnoreCase("") || myPlotRequestModels.get(position).getEmpComment().trim().equalsIgnoreCase("null")) {
                holder.emp_remark.setText("Pending Status");
                if (myPlotRequestModels.get(position).getStatus().equalsIgnoreCase("Avilable")) {
                    holder.emp_remark.setText("Available Now");
                } else if (myPlotRequestModels.get(position).getStatus().equalsIgnoreCase("Rejected")) {
                    holder.emp_remark.setText("Not Available");
                } else if (myPlotRequestModels.get(position).getStatus().equalsIgnoreCase("Pending")) {
                    holder.emp_remark.setText("Wait For Response");
                }
            }
            holder.amounts.setText("Amount - To be finalized");
            holder.customers.setText(myPlotRequestModels.get(position).getRequestedcustomerName());
            holder.status_txt.setText(myPlotRequestModels.get(position).getStatus());
            if (myPlotRequestModels.get(position).getStatus().equalsIgnoreCase("Avilable")) {
                holder.status_txt.setTextColor(Color.parseColor("#05c505"));
                holder.status_img.setImageResource(R.drawable.approved);
                holder.amounts.setText("Amount - \u20b9 " + myPlotRequestModels.get(position).getAmount());
                holder.status_txt.setText("Available");
                holder.buy_plots.setVisibility(View.VISIBLE);
                if ((!myPlotRequestModels.get(position).getTotalPayments().equalsIgnoreCase("0")) && (!myPlotRequestModels.get(position).getTotalPayments().equalsIgnoreCase("")) && (!myPlotRequestModels.get(position).getTotalPayments().equalsIgnoreCase("null"))) {
                    holder.status_txt.setText("Payment Request Sent");
                    holder.status_txt.setTextColor(Color.parseColor("#05c505"));
                    holder.status_img.setImageResource(R.drawable.approved);
                    holder.buy_plots.setVisibility(View.INVISIBLE);
                }
            } else if (myPlotRequestModels.get(position).getStatus().equalsIgnoreCase("Rejected")) {
                holder.status_txt.setTextColor(Color.parseColor("#ff0000"));
                holder.amounts.setText("Amount - To be finalized");
                holder.status_img.setImageResource(R.drawable.rejected);
                holder.buy_plots.setVisibility(View.INVISIBLE);
            } else if (myPlotRequestModels.get(position).getStatus().equalsIgnoreCase("Pending")) {
                holder.status_txt.setTextColor(Color.parseColor("#FFAF49"));
                holder.amounts.setText("Amount - To be finalized");
                holder.status_img.setImageResource(R.drawable.pending);
                holder.buy_plots.setVisibility(View.INVISIBLE);
            } else if (myPlotRequestModels.get(position).getStatus().equalsIgnoreCase("7 Days Over")) {
                holder.status_txt.setTextColor(Color.parseColor("#ff0000"));
                holder.status_txt.setText("Request Expired");
                holder.amounts.setText("Amount - Request Expired");
                holder.status_img.setImageResource(R.drawable.rejected);
                holder.buy_plots.setVisibility(View.INVISIBLE);
            } else if (myPlotRequestModels.get(position).getStatus().equalsIgnoreCase("Sold")) {
                holder.status_txt.setTextColor(Color.parseColor("#ff0000"));
                holder.status_txt.setText("Plot Sold");
                holder.status_img.setImageResource(R.drawable.rejected);
                holder.buy_plots.setVisibility(View.INVISIBLE);
            }

            holder.view_request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ViewRequestActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("PM", myPlotRequestModels.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            holder.buy_plots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), AgentPaymentActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("PM", myPlotRequestModels.get(position));
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 29);
                }
            });
        }

        @Override
        public RequestHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.agent_plots_req_row, viewGroup, false);
            RequestHolder listHolder = new RequestHolder(mainGroup);
            return listHolder;
        }

        public class RequestHolder extends RecyclerView.ViewHolder {

            TextView plot_name, your_rem, emp_remark, status_txt, date_txt, view_request, buy_plots, amounts, customers;
            ImageView status_img;

            public RequestHolder(View view) {
                super(view);
                customers = (TextView) view.findViewById(R.id.customers);
                amounts = (TextView) view.findViewById(R.id.amounts);
                plot_name = (TextView) view.findViewById(R.id.plot_name);
                your_rem = (TextView) view.findViewById(R.id.your_rem);
                emp_remark = (TextView) view.findViewById(R.id.emp_remark);
                status_txt = (TextView) view.findViewById(R.id.status_txt);
                date_txt = (TextView) view.findViewById(R.id.date_txt);
                view_request = (TextView) view.findViewById(R.id.view_request);
                buy_plots = (TextView) view.findViewById(R.id.buy_plots);
                status_img = (ImageView) view.findViewById(R.id.status_img);
            }
        }
    }

    public String getForDate(String tt) {
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat format2 = new SimpleDateFormat("dd MMM, yyyy");
            Date date = format1.parse(tt);
            return format2.format(date);
        } catch (Exception e) {
            return tt;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 29 && resultCode == RESULT_OK) {
            getAllRequest();
        }
    }

    public int getIds(String s) {
        try {
            int n = Integer.parseInt(s);
            return n;
        } catch (Exception e) {
            return 0;
        }
    }
}